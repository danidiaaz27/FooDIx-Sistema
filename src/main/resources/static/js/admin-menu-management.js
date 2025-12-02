/**
 * ========================================
 * GESTIÓN DE MENÚ DEL RESTAURANTE
 * ========================================
 * Sistema para gestionar platos y sus unidades de medida
 * Conectado a Base de Datos via Backend
 */

// Contador global de unidades de medida
let measurementUnitCounter = 1;

// Variable para almacenar el plato que se está editando
let currentEditingDishId = null;

// Catálogo de tipos de unidades de medida
let tiposUnidadMedida = [];

/**
 * Obtener token CSRF para peticiones POST/PUT/DELETE
 */
function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || '';
}

/**
 * Cargar tipos de unidades de medida desde el backend
 */
async function loadTiposUnidadMedida() {
    try {
        const response = await fetch('/api/tipos-unidad-medida', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error('Error al cargar tipos de unidades');
        }
        
        tiposUnidadMedida = await response.json();
        console.log('Tipos de unidades cargados:', tiposUnidadMedida);
    } catch (error) {
        console.error('Error al cargar tipos de unidades:', error);
        alert('⚠️ Error al cargar los tipos de unidades de medida');
    }
}

/**
 * Inicialización del modal de gestión de menú
 */
document.addEventListener('DOMContentLoaded', function() {
    // Cargar tipos de unidades al iniciar
    loadTiposUnidadMedida();
    
    const manageMenuModal = document.getElementById('manageMenuModal');
    
    if (manageMenuModal) {
        manageMenuModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const restaurantId = button.getAttribute('data-id');
            const restaurantName = button.getAttribute('data-nombre');
            
            // Actualizar información del modal
            document.getElementById('manageMenuRestaurantId').value = restaurantId;
            document.getElementById('manageMenuRestaurantName').textContent = restaurantName;
            
            // Cargar menú del restaurante
            loadRestaurantMenu(restaurantId);
        });
    }
    
    // Listener para el modal de agregar/editar plato
    const addDishModal = document.getElementById('addDishModal');
    if (addDishModal) {
        addDishModal.addEventListener('hidden.bs.modal', function () {
            // Resetear cuando se cierra el modal
            currentEditingDishId = null;
            document.getElementById('addDishForm').reset();
            document.getElementById('measurementUnitsContainer').innerHTML = '';
        });
    }
});

/**
 * Cargar menú del restaurante desde el backend
 * @param {string} restaurantId - ID del restaurante
 */
function loadRestaurantMenu(restaurantId) {
    console.log('Cargando menú para restaurante:', restaurantId);
    const container = document.getElementById('menuDishesContainer');
    const noItemsMessage = document.getElementById('noMenuItemsMessage');
    
    // Mostrar indicador de carga
    container.innerHTML = `
        <div class="text-center py-5">
            <div class="spinner-border text-danger" role="status">
                <span class="visually-hidden">Cargando...</span>
            </div>
            <p class="text-muted mt-3">Cargando menú del restaurante...</p>
        </div>
    `;
    
    // Llamada al backend con timestamp para evitar cache
    fetch(`/api/platos/restaurante/${restaurantId}?t=${new Date().getTime()}`)
        .then(response => {
            console.log('Response status:', response.status);
            if (!response.ok) {
                throw new Error('Error al cargar el menú');
            }
            return response.json();
        })
        .then(data => {
            console.log('Platos recibidos:', data);
            if (data && data.length > 0) {
                renderMenuDishes(data);
                if (noItemsMessage) {
                    noItemsMessage.style.display = 'none';
                }
            } else {
                container.innerHTML = '';
                if (noItemsMessage) {
                    noItemsMessage.style.display = 'block';
                }
            }
        })
        .catch(error => {
            console.error('Error completo:', error);
            container.innerHTML = `
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-info-circle"></i> 
                    No se pudieron cargar los platos. Puede comenzar agregando el primer plato del menú.
                </div>
            `;
            if (noItemsMessage) {
                noItemsMessage.style.display = 'block';
            }
        });
}

/**
 * Renderizar la lista de platos en el DOM
 * @param {Array} dishes - Array de platos con sus unidades
 */
function renderMenuDishes(dishes) {
    const container = document.getElementById('menuDishesContainer');
    container.innerHTML = '';
    
    dishes.forEach(dish => {
        const units = dish.unidadesMedida || [];
        const minPrice = units.length > 0 ? Math.min(...units.map(u => u.precioOriginal)) : 0;
        const maxPrice = units.length > 0 ? Math.max(...units.map(u => u.precioOriginal)) : 0;
        
        const dishHtml = `
            <div class="card shadow-sm mb-3" style="border-radius: 12px; border: 1px solid #e0e0e0;">
                <div class="card-header" style="background-color: white; border-bottom: 2px solid #f5f5f5; padding: 20px;">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-1" style="color: #333; font-weight: 600;">${dish.nombre}</h5>
                            <small class="text-muted">
                                ${units.length} unidad(es) disponible(s) • 
                                Rango de precios: S/ ${minPrice.toFixed(2)} - S/ ${maxPrice.toFixed(2)}
                            </small>
                        </div>
                        <div>
                            <button class="btn btn-sm btn-warning me-2" onclick="editDish(${dish.codigo})">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn btn-sm btn-danger" onclick="deleteDish(${dish.codigo}, '${dish.nombre.replace(/'/g, "\\'")}')">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="card-body" style="padding: 20px; background-color: #fafafa;">
                    <div class="row g-3">
                        ${renderDishUnits(units)}
                    </div>
                </div>
            </div>
        `;
        
        container.insertAdjacentHTML('beforeend', dishHtml);
    });
}

/**
 * Renderizar las unidades de un plato
 * @param {Array} units - Array de unidades de medida
 * @returns {string} HTML de las unidades
 */
function renderDishUnits(units) {
    if (!units || units.length === 0) {
        return '<div class="col-12"><p class="text-muted">Sin unidades definidas</p></div>';
    }
    
    return units.map(unit => `
        <div class="col-md-${units.length <= 2 ? '6' : '4'}">
            <div class="p-3" style="background-color: white; border-radius: 8px; border: 1px solid #e0e0e0;">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span style="font-weight: 600; color: #555;">${unit.nombreTipoUnidad}</span>
                    <span class="badge badge-price">S/ ${unit.precioOriginal.toFixed(2)}</span>
                </div>
                ${unit.descripcion ? `<small class="text-muted">${unit.descripcion}</small>` : ''}
            </div>
        </div>
    `).join('');
}

/**
 * Abrir modal para agregar un nuevo plato
 */
function openAddDishModal() {
    currentEditingDishId = null;
    
    // Resetear el formulario
    document.getElementById('addDishForm').reset();
    document.getElementById('addDishModalLabel').innerHTML = '<i class="fas fa-plus-circle"></i> Agregar Nuevo Plato';
    
    // Limpiar unidades y agregar una por defecto
    const container = document.getElementById('measurementUnitsContainer');
    container.innerHTML = '';
    measurementUnitCounter = 1;
    addMeasurementUnit();
    
    // Mostrar el modal
    const modal = new bootstrap.Modal(document.getElementById('addDishModal'));
    modal.show();
}

/**
 * Cerrar modal de agregar/editar plato
 */
function closeAddDishModal() {
    const modalElement = document.getElementById('addDishModal');
    const modal = bootstrap.Modal.getInstance(modalElement);
    if (modal) {
        modal.hide();
    }
    // El listener 'hidden.bs.modal' se encargará de limpiar
}

/**
 * Agregar una nueva unidad de medida al formulario
 */
function addMeasurementUnit() {
    const container = document.getElementById('measurementUnitsContainer');
    
    // Generar opciones del combobox desde los tipos cargados
    let optionsHtml = '<option value="">-- Seleccione una unidad --</option>';
    tiposUnidadMedida.forEach(tipo => {
        optionsHtml += `<option value="${tipo.codigo}">${tipo.nombre}</option>`;
    });
    
    const unitHtml = `
        <div class="measurement-unit-item card mb-3 shadow-sm" style="border-radius: 10px; border: 2px solid #e0e0e0;">
            <div class="card-body" style="background-color: white; border-radius: 10px;">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h6 class="mb-0" style="color: #555;">Unidad #${measurementUnitCounter}</h6>
                    <button type="button" class="btn btn-sm btn-danger" onclick="removeMeasurementUnit(this)" style="border-radius: 50%; width: 32px; height: 32px; padding: 0;">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Tipo de Unidad <span class="text-danger">*</span></label>
                        <select class="form-select unit-type" required style="border-radius: 8px;">
                            ${optionsHtml}
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Precio Original (S/) <span class="text-danger">*</span></label>
                        <input type="number" class="form-control unit-price" 
                               placeholder="Ej: 18.00" step="0.01" min="0"
                               required style="border-radius: 8px;">
                    </div>
                    <div class="col-12">
                        <label class="form-label">Descripción (opcional)</label>
                        <input type="text" class="form-control unit-description" 
                               placeholder="Ej: Porción individual, Para 4-6 personas"
                               style="border-radius: 8px;">
                    </div>
                </div>
            </div>
        </div>
    `;
    
    container.insertAdjacentHTML('beforeend', unitHtml);
    measurementUnitCounter++;
}

/**
 * Eliminar una unidad de medida del formulario
 * @param {HTMLElement} button - Botón que disparó la acción
 */
function removeMeasurementUnit(button) {
    const container = document.getElementById('measurementUnitsContainer');
    const items = container.querySelectorAll('.measurement-unit-item');
    
    // No permitir eliminar si solo hay una unidad
    if (items.length <= 1) {
        alert('⚠️ Debe haber al menos una unidad de medida para el plato');
        return;
    }
    
    // Confirmar eliminación
    if (confirm('¿Está seguro de eliminar esta unidad de medida?')) {
        button.closest('.measurement-unit-item').remove();
        
        // Renumerar las unidades restantes
        renumberMeasurementUnits();
    }
}

/**
 * Renumerar las unidades de medida en el formulario
 */
function renumberMeasurementUnits() {
    const container = document.getElementById('measurementUnitsContainer');
    const remainingItems = container.querySelectorAll('.measurement-unit-item');
    
    remainingItems.forEach((item, index) => {
        item.querySelector('h6').textContent = `Unidad #${index + 1}`;
    });
}

/**
 * Guardar plato (crear o actualizar) usando API backend
 */
async function saveDish() {
    const dishName = document.getElementById('dishName').value.trim();
    
    if (!dishName) {
        alert('⚠️ Por favor ingrese el nombre del plato');
        return;
    }
    
    // Recolectar todas las unidades de medida
    const units = [];
    const unitItems = document.querySelectorAll('.measurement-unit-item');
    
    let isValid = true;
    unitItems.forEach((item, index) => {
        const tipoUnidadSelect = item.querySelector('.unit-type');
        const codigoTipoUnidad = tipoUnidadSelect.value;
        const price = item.querySelector('.unit-price').value;
        const description = item.querySelector('.unit-description').value.trim();
        
        if (!codigoTipoUnidad || !price) {
            alert(`⚠️ Complete todos los campos obligatorios de la Unidad #${index + 1}`);
            isValid = false;
            return;
        }
        
        if (parseFloat(price) <= 0) {
            alert(`⚠️ El precio de la Unidad #${index + 1} debe ser mayor a 0`);
            isValid = false;
            return;
        }
        
        units.push({
            codigoTipoUnidad: parseInt(codigoTipoUnidad),
            descripcion: description,
            precioOriginal: parseFloat(price)
        });
    });
    
    if (!isValid || units.length === 0) {
        return;
    }
    
    const restaurantId = document.getElementById('manageMenuRestaurantId').value;
    
    // Deshabilitar botón mientras se guarda
    const saveButton = event.target;
    const originalText = saveButton.innerHTML;
    saveButton.disabled = true;
    saveButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Guardando...';
    
    try {
        const dishData = {
            codigoRestaurante: parseInt(restaurantId),
            nombre: dishName,
            descripcion: '',
            unidadesMedida: units
        };
        
        const url = currentEditingDishId 
            ? `/api/platos/${currentEditingDishId}` 
            : '/api/platos';
        const method = currentEditingDishId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCsrfToken()
            },
            body: JSON.stringify(dishData)
        });
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Error al guardar el plato');
        }
        
        const savedDish = await response.json();
        console.log('Plato guardado:', savedDish);
        
        saveButton.disabled = false;
        saveButton.innerHTML = originalText;
        
        const wasEditing = currentEditingDishId !== null;
        
        // Cerrar modal de agregar/editar primero
        closeAddDishModal();
        
        // Mostrar mensaje de éxito
        alert(`✅ Plato ${wasEditing ? 'actualizado' : 'guardado'} exitosamente`);
        
        // Recargar menú (el modal de gestión de menú sigue abierto)
        console.log('Recargando menú del restaurante:', restaurantId);
        loadRestaurantMenu(restaurantId);
        
    } catch (error) {
        saveButton.disabled = false;
        saveButton.innerHTML = originalText;
        console.error('Error al guardar plato:', error);
        alert(`❌ Error al guardar el plato: ${error.message}`);
    }
}

/**
 * Editar un plato existente desde la API backend
 * @param {number} dishId - ID del plato a editar
 */
async function editDish(dishId) {
    currentEditingDishId = dishId;
    
    // Actualizar título del modal
    document.getElementById('addDishModalLabel').innerHTML = '<i class="fas fa-edit"></i> Editar Plato';
    
    try {
        // Cargar datos del plato desde la API
        const response = await fetch(`/api/platos/${dishId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error('Error al cargar los datos del plato');
        }
        
        const dish = await response.json();
        
        // Rellenar el formulario con los datos del plato
        document.getElementById('dishName').value = dish.nombre;
        
        // Limpiar y llenar las unidades
        const container = document.getElementById('measurementUnitsContainer');
        container.innerHTML = '';
        measurementUnitCounter = 1;
        
        const units = dish.unidadesMedida || [];
        if (units.length > 0) {
            units.forEach(unit => {
                addMeasurementUnit();
                const lastUnit = container.querySelector('.measurement-unit-item:last-child');
                lastUnit.querySelector('.unit-type').value = unit.codigoTipoUnidad;
                lastUnit.querySelector('.unit-price').value = unit.precioOriginal;
                lastUnit.querySelector('.unit-description').value = unit.descripcion || '';
            });
        } else {
            // Si no hay unidades, agregar una vacía
            addMeasurementUnit();
        }
        
        // Mostrar el modal
        const modal = new bootstrap.Modal(document.getElementById('addDishModal'));
        modal.show();
        
    } catch (error) {
        console.error('Error al cargar plato:', error);
        alert('❌ Error al cargar los datos del plato');
    }
}

/**
 * Eliminar un plato usando API backend
 * @param {number} dishId - ID del plato a eliminar
 * @param {string} dishName - Nombre del plato (para confirmación)
 */
async function deleteDish(dishId, dishName) {
    if (!confirm(`¿Está seguro de eliminar el plato "${dishName}" del menú?\n\nEsta acción no se puede deshacer.`)) {
        return;
    }
    
    const restaurantId = document.getElementById('manageMenuRestaurantId').value;
    
    try {
        const response = await fetch(`/api/platos/${dishId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCsrfToken()
            }
        });
        
        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || 'Error al eliminar el plato');
        }
        
        console.log('Plato eliminado correctamente');
        alert('✅ Plato eliminado exitosamente');
        
        // Recargar menú
        loadRestaurantMenu(restaurantId);
        
    } catch (error) {
        console.error('Error al eliminar plato:', error);
        alert(`❌ Error al eliminar el plato: ${error.message}`);
    }
}

// ========================================
// EXPORTAR FUNCIONES AL SCOPE GLOBAL
// ========================================
window.openAddDishModal = openAddDishModal;
window.closeAddDishModal = closeAddDishModal;
window.addMeasurementUnit = addMeasurementUnit;
window.removeMeasurementUnit = removeMeasurementUnit;
window.saveDish = saveDish;
window.editDish = editDish;
window.deleteDish = deleteDish;
