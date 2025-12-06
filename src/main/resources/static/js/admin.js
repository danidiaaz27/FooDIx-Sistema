/**
 * ========================================
 * FOODIX - PANEL DE ADMINISTRACIÓN
 * JavaScript Principal
 * ========================================
 */

// ========================================
// 1. FUNCIONES GLOBALES DE NAVEGACIÓN
// ========================================

/**
 * Cambia entre las diferentes secciones del panel
 */
function showSection(sectionName) {
    console.log('>>> Cambiando a sección:', sectionName);
    
    // Ocultar todas las secciones
    document.querySelectorAll('.menu-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Mostrar la sección seleccionada
    const targetSection = document.getElementById(sectionName + '-content');
    if (targetSection) {
        targetSection.classList.add('active');
        console.log('✓ Sección mostrada:', sectionName + '-content');
    } else {
        console.error('✗ No se encontró la sección:', sectionName + '-content');
    }
    
    // Actualizar links activos en el sidebar
    document.querySelectorAll('.sidebar .nav-link').forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('data-section') === sectionName) {
            link.classList.add('active');
        }
    });
}

// ========================================
// 2. GESTIÓN DE RESTAURANTES
// ========================================

function aprobarRestaurante(button) {
    const id = button.dataset.id;
    const nombre = button.dataset.nombre;
    
    if (confirm(`¿Está seguro de aprobar el restaurante "${nombre}"?\n\nEsto permitirá que puedan acceder al sistema.`)) {
        submitFormWithJWT(`/menuAdministrador/restaurant/${id}/approve`, 'restaurantes');
    }
}

function eliminarRestaurante(button) {
    const id = button.dataset.id;
    const nombre = button.dataset.nombre;
    
    if (confirm(`⚠️ ADVERTENCIA ⚠️\n\n¿Está seguro de ELIMINAR el restaurante "${nombre}"?\n\nEsta acción NO se puede deshacer.`)) {
        submitFormWithJWT(`/menuAdministrador/restaurant/${id}/delete`);
    }
}

function verDocumento(tipoDocumento, restauranteId) {
    const url = `/uploads/restaurante/${restauranteId}/${tipoDocumento}`;
    window.open(url, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
}

// ========================================
// 3. UTILIDADES - FORMULARIOS CON CSRF
// ========================================

// Variables para el modal de cambio de estado
let pendingUserToggle = null;
let pendingRestaurantToggle = null;
let pendingDeliveryAvailabilityToggle = null;
let pendingDeliveryStatusToggle = null;
let pendingCategoryToggle = null;
let pendingCategoryDelete = null;

/**
 * Cambia el estado de un usuario o restaurante mediante AJAX sin recargar la página
 */
function toggleStatusViaAjax(url, buttonElement, currentStatus) {
    // Obtener JWT token del localStorage
    const jwtToken = localStorage.getItem('jwt_token');
    
    // Agregar animación de loading al botón
    buttonElement.classList.add('switching');
    buttonElement.disabled = true;
    
    // Realizar petición AJAX con JWT
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/x-www-form-urlencoded',
        }
    })
    .then(response => {
        if (response.ok) {
            // Invertir el estado
            const newStatus = !currentStatus;
            
            // Actualizar el botón
            updateToggleButton(buttonElement, newStatus);
            
            // Actualizar el badge de estado en la misma fila
            updateStatusBadge(buttonElement, newStatus);
            
            // Mostrar mensaje de éxito
            showToast('success', `Estado cambiado exitosamente a ${newStatus ? 'Activo' : 'Inactivo'}`);
        } else {
            throw new Error('Error al cambiar el estado');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('error', 'Error al cambiar el estado. Por favor, intente nuevamente.');
    })
    .finally(() => {
        // Quitar animación y re-habilitar botón
        buttonElement.classList.remove('switching');
        buttonElement.disabled = false;
        
        // Mantener el foco en el botón
        buttonElement.focus();
    });
}

/**
 * Actualiza visualmente el botón de toggle
 */
function updateToggleButton(button, newStatus) {
    const icon = button.querySelector('i');
    
    // Detectar tipo de botón por el icono actual
    const isAvailabilityToggle = icon.classList.contains('fa-user-check') || icon.classList.contains('fa-user-times');
    const isStatusToggle = icon.classList.contains('fa-toggle-on') || icon.classList.contains('fa-toggle-off');
    
    if (isAvailabilityToggle) {
        // Botón de disponibilidad (repartidor)
        if (newStatus) {
            button.classList.remove('btn-secondary');
            button.classList.add('btn-info');
            button.title = 'Marcar no disponible';
            
            icon.classList.remove('fa-user-times');
            icon.classList.add('fa-user-check');
        } else {
            button.classList.remove('btn-info');
            button.classList.add('btn-secondary');
            button.title = 'Marcar disponible';
            
            icon.classList.remove('fa-user-check');
            icon.classList.add('fa-user-times');
        }
    } else if (isStatusToggle) {
        // Botón de estado (activar/desactivar)
        if (newStatus) {
            button.classList.remove('btn-secondary');
            button.classList.add('btn-success');
            
            // Determinar el tipo de entidad
            if (button.title.includes('usuario') || button.getAttribute('onclick')?.includes('toggleUserStatus')) {
                button.title = 'Desactivar usuario';
            } else if (button.title.includes('restaurante') || button.getAttribute('onclick')?.includes('toggleRestaurantStatus')) {
                button.title = 'Desactivar restaurante';
            } else {
                button.title = 'Desactivar';
            }
            
            icon.classList.remove('fa-toggle-off');
            icon.classList.add('fa-toggle-on');
        } else {
            button.classList.remove('btn-success');
            button.classList.add('btn-secondary');
            
            // Determinar el tipo de entidad
            if (button.title.includes('usuario') || button.getAttribute('onclick')?.includes('toggleUserStatus')) {
                button.title = 'Activar usuario';
            } else if (button.title.includes('restaurante') || button.getAttribute('onclick')?.includes('toggleRestaurantStatus')) {
                button.title = 'Activar restaurante';
            } else {
                button.title = 'Activar';
            }
            
            icon.classList.remove('fa-toggle-on');
            icon.classList.add('fa-toggle-off');
        }
    }
    
    // Actualizar el onclick para reflejar el nuevo estado
    const onclickAttr = button.getAttribute('onclick');
    if (onclickAttr) {
        const updatedOnclick = onclickAttr.replace(
            /, (true|false),/,
            `, ${newStatus},`
        );
        button.setAttribute('onclick', updatedOnclick);
    }
}

/**
 * Actualiza el badge de estado en la tabla
 */
function updateStatusBadge(button, newStatus) {
    // Encontrar la fila del botón
    const row = button.closest('tr');
    if (!row) return;
    
    // Determinar qué tipo de badge actualizar según el icono del botón
    const icon = button.querySelector('i');
    const isAvailabilityToggle = icon && (icon.classList.contains('fa-user-check') || icon.classList.contains('fa-user-times'));
    const isStatusToggle = icon && (icon.classList.contains('fa-toggle-on') || icon.classList.contains('fa-toggle-off'));
    
    // Buscar el badge correcto según el tipo de toggle
    const cells = row.querySelectorAll('td');
    cells.forEach(cell => {
        const badge = cell.querySelector('.badge');
        if (!badge) return;
        
        const badgeText = badge.textContent.trim();
        
        // Si es toggle de disponibilidad, actualizar badge de Disponible/No disponible
        if (isAvailabilityToggle && (badgeText === 'Disponible' || badgeText === 'No disponible')) {
            if (newStatus) {
                badge.classList.remove('bg-secondary');
                badge.classList.add('bg-success');
                badge.textContent = 'Disponible';
            } else {
                badge.classList.remove('bg-success');
                badge.classList.add('bg-secondary');
                badge.textContent = 'No disponible';
            }
        }
        // Si es toggle de estado, actualizar badge de Activo/Inactivo
        else if (isStatusToggle && (badgeText === 'Activo' || badgeText === 'Inactivo')) {
            if (newStatus) {
                badge.classList.remove('bg-danger');
                badge.classList.add('bg-success');
                badge.textContent = 'Activo';
            } else {
                badge.classList.remove('bg-success');
                badge.classList.add('bg-danger');
                badge.textContent = 'Inactivo';
            }
        }
    });
}

/**
 * Muestra un mensaje toast temporal
 */
function showToast(type, message) {
    // Crear elemento toast
    const toast = document.createElement('div');
    toast.className = `alert alert-${type === 'success' ? 'success' : 'danger'} position-fixed`;
    toast.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px; animation: slideInRight 0.3s ease-out;';
    toast.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
        ${message}
    `;
    
    // Agregar al body
    document.body.appendChild(toast);
    
    // Remover después de 3 segundos
    setTimeout(() => {
        toast.style.animation = 'slideOutRight 0.3s ease-out';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function toggleUserStatus(userId, currentStatus, buttonElement) {
    const modal = document.getElementById('toggleUserStatusModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingUserToggle = { userId, currentStatus, buttonElement };
    
    // Configurar textos según el estado actual
    const action = currentStatus ? 'desactivar' : 'activar';
    const statusText = currentStatus ? 'desactivado' : 'activado';
    
    // Actualizar contenido del modal
    document.getElementById('toggleUserStatusMessage').textContent = 
        `¿Está seguro de ${action} este usuario? El usuario será ${statusText} en el sistema.`;
    document.getElementById('toggleUserStatusName').textContent = `ID: ${userId}`;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function toggleRestaurantStatus(restaurantId, currentStatus, buttonElement) {
    const modal = document.getElementById('toggleRestaurantStatusModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingRestaurantToggle = { restaurantId, currentStatus, buttonElement };
    
    // Configurar textos según el estado actual
    const action = currentStatus ? 'desactivar' : 'activar';
    const statusText = currentStatus ? 'desactivado' : 'activado';
    
    // Actualizar contenido del modal
    document.getElementById('toggleRestaurantStatusMessage').textContent = 
        `¿Está seguro de ${action} este restaurante? El restaurante será ${statusText} en el sistema.`;
    document.getElementById('toggleRestaurantStatusId').textContent = restaurantId;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function toggleDeliveryAvailability(deliveryId, currentStatus, buttonElement) {
    const modal = document.getElementById('toggleDeliveryAvailabilityModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingDeliveryAvailabilityToggle = { deliveryId, currentStatus, buttonElement };
    
    // Configurar textos según el estado actual
    const action = currentStatus ? 'marcar como NO disponible' : 'marcar como disponible';
    const statusText = currentStatus ? 'NO estará disponible' : 'estará disponible';
    
    // Actualizar contenido del modal
    document.getElementById('toggleDeliveryAvailabilityMessage').textContent = 
        `¿Está seguro de ${action} a este repartidor? El repartidor ${statusText} para entregas.`;
    document.getElementById('toggleDeliveryAvailabilityId').textContent = deliveryId;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function toggleDeliveryStatus(deliveryId, currentStatus, buttonElement) {
    const modal = document.getElementById('toggleDeliveryStatusModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingDeliveryStatusToggle = { deliveryId, currentStatus, buttonElement };
    
    // Configurar textos según el estado actual
    const action = currentStatus ? 'desactivar' : 'activar';
    const statusText = currentStatus ? 'desactivado' : 'activado';
    
    // Actualizar contenido del modal
    document.getElementById('toggleDeliveryStatusMessage').textContent = 
        `¿Está seguro de ${action} a este repartidor? El repartidor será ${statusText} en el sistema.`;
    document.getElementById('toggleDeliveryStatusId').textContent = deliveryId;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function toggleCategoryStatus(categoryId, currentStatus, buttonElement) {
    const modal = document.getElementById('toggleCategoryStatusModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingCategoryToggle = { categoryId, currentStatus, buttonElement };
    
    // Configurar textos según el estado actual
    const action = currentStatus ? 'desactivar' : 'activar';
    const statusText = currentStatus ? 'desactivada' : 'activada';
    
    // Actualizar contenido del modal
    document.getElementById('toggleCategoryStatusMessage').textContent = 
        `¿Está seguro de ${action} esta categoría? La categoría será ${statusText} en el sistema.`;
    document.getElementById('toggleCategoryStatusId').textContent = categoryId;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function deleteCategoryConfirm(categoryId, categoryName) {
    const modal = document.getElementById('deleteCategoryModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingCategoryDelete = { categoryId, categoryName };
    
    // Actualizar contenido del modal
    document.getElementById('deleteCategoryMessage').textContent = 
        `¿Está seguro de eliminar esta categoría? Esta acción no se puede deshacer.`;
    document.getElementById('deleteCategoryName').textContent = categoryName;
    document.getElementById('deleteCategoryId').textContent = categoryId;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function submitFormWithJWT(action, section = null) {
    // Obtener JWT token
    const jwtToken = localStorage.getItem('jwt_token');
    
    // Crear formulario
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = action;
    
    // Agregar JWT como header (mediante un input oculto no funciona, usaremos fetch)
    // En su lugar, vamos a usar fetch con JWT
    fetch(action, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: section ? `section=${encodeURIComponent(section)}` : ''
    })
    .then(response => {
        if (response.ok) {
            // Recargar la página para ver los cambios
            window.location.reload();
        } else {
            throw new Error('Error en la operación');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('error', 'Error al realizar la operación. Por favor, intente nuevamente.');
    });
}

// ========================================
// 4. INICIALIZACIÓN AL CARGAR EL DOM
// ========================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('=== MENU ADMINISTRADOR CARGADO ===');
    console.log('showSection está definida:', typeof showSection);
    
    // Inicializar módulos
    initConfirmaciones();
    initModales();
    initFiltros();
    initPasswordToggles();
    initValidaciones();
    initPermisos();
    initSeccionActiva();
    initToggleUserStatusModal();
    initToggleRestaurantStatusModal();
    initToggleDeliveryAvailabilityModal();
    initToggleDeliveryStatusModal();
    initToggleCategoryStatusModal();
    initDeleteCategoryModal();
    
    console.log('=== INICIALIZACIÓN COMPLETADA ===');
});

// ========================================
// 5. MÓDULO: CONFIRMACIONES
// ========================================

function initConfirmaciones() {
    document.body.addEventListener('submit', function(e) {
        const form = e.target;
        
        if (form.action && form.action.includes('/approve')) {
            if (!confirm('¿Está seguro de aprobar este repartidor?')) {
                e.preventDefault();
                return false;
            }
        }
        
        if (form.action && form.action.includes('/reject')) {
            if (!confirm('¿Está seguro de rechazar este repartidor?')) {
                e.preventDefault();
                return false;
            }
        }
    });
}

// ========================================
// 6. MÓDULO: MODALES
// ========================================

function initModales() {
    initModalEditarUsuario();
    initModalVerCliente();
    initModalVerRestaurante();
    initModalRechazarRestaurante();
    initModalRechazarRepartidor();
    initModalEditarDelivery();
    initModalEditarRestaurante();
    initModalEditarRol();
}

function initModalEditarUsuario() {
    const modal = document.getElementById('editUserModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        document.getElementById('editUserName').value = button.getAttribute('data-name');
        document.getElementById('editUserLastName').value = button.getAttribute('data-lastname');
        document.getElementById('editUserEmail').value = button.getAttribute('data-email');
        document.getElementById('editUserPhone').value = button.getAttribute('data-phone') || '';
        document.getElementById('editUserAddress').value = button.getAttribute('data-address') || '';
        
        document.getElementById('editUserForm').action = '/menuAdministrador/user/' + button.getAttribute('data-id') + '/update';
    });
}

function initModalVerCliente() {
    const modal = document.getElementById('viewClientModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        document.getElementById('viewClientName').textContent = button.getAttribute('data-name') || 'N/A';
        document.getElementById('viewClientDni').textContent = button.getAttribute('data-dni') || 'N/A';
        document.getElementById('viewClientEmail').textContent = button.getAttribute('data-email') || 'N/A';
        document.getElementById('viewClientPhone').textContent = button.getAttribute('data-phone') || 'N/A';
        document.getElementById('viewClientAddress').textContent = button.getAttribute('data-address') || 'N/A';
    });
}

function initModalVerRestaurante() {
    const modal = document.getElementById('viewRestaurantModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const restaurantId = button.getAttribute('data-id');
        modal.dataset.restaurantId = restaurantId;
        
        document.getElementById('viewRestaurantNombre').textContent = button.getAttribute('data-nombre') || 'N/A';
        document.getElementById('viewRestaurantRazon').textContent = button.getAttribute('data-razon') || 'N/A';
        document.getElementById('viewRestaurantRuc').textContent = button.getAttribute('data-ruc') || 'N/A';
        document.getElementById('viewRestaurantEmail').textContent = button.getAttribute('data-email') || 'N/A';
        document.getElementById('viewRestaurantTelefono').textContent = button.getAttribute('data-telefono') || 'N/A';
        document.getElementById('viewRestaurantDireccion').textContent = button.getAttribute('data-direccion') || 'N/A';
        
        // Configurar botones de documentos con las rutas estándar del sistema
        const btnVerCarta = document.getElementById('btnVerCarta');
        const btnVerSanidad = document.getElementById('btnVerSanidad');
        const btnVerLicencia = document.getElementById('btnVerLicencia');
        
        // Las rutas siguen el patrón: restaurante/{id}/{TIPO_DOCUMENTO}.{extension}
        const docTypes = ['CARTA_RESTAURANTE', 'CarnetSanidad', 'LicenciaFuncionamiento'];
        const buttons = [btnVerCarta, btnVerSanidad, btnVerLicencia];
        
        buttons.forEach((btn, index) => {
            if (btn) {
                // Construir ruta basada en el patrón del sistema de archivos
                const ruta = `restaurante/${restaurantId}/${docTypes[index]}`;
                btn.dataset.ruta = ruta;
                // Los botones siempre están habilitados; si el archivo no existe, el backend devolverá 404
                btn.disabled = false;
                btn.classList.remove('btn-outline-secondary');
                btn.classList.add('btn-outline-primary');
            }
        });
    });
}

function initModalRechazarRestaurante() {
    const modal = document.getElementById('rejectRestaurantModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        document.getElementById('rejectRestaurantNombre').textContent = button.getAttribute('data-nombre');
        document.getElementById('motivoRechazo').value = '';
        document.getElementById('rejectRestaurantForm').action = '/menuAdministrador/restaurant/' + button.getAttribute('data-id') + '/reject';
    });
}

function initModalRechazarRepartidor() {
    const modal = document.getElementById('rejectDeliveryModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        document.getElementById('rejectDeliveryNombre').textContent = button.getAttribute('data-name');
        document.getElementById('motivoRechazoDelivery').value = '';
        document.getElementById('rejectDeliveryForm').action = '/menuAdministrador/repartidor/' + button.getAttribute('data-id') + '/reject';
    });
}

function initModalEditarDelivery() {
    const modal = document.getElementById('editDeliveryModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        document.getElementById('editDeliveryName').value = button.getAttribute('data-name');
        document.getElementById('editDeliveryLastName').value = button.getAttribute('data-lastname');
        document.getElementById('editDeliveryEmail').value = button.getAttribute('data-email');
        document.getElementById('editDeliveryPhone').value = button.getAttribute('data-phone') || '';
        document.getElementById('editDeliveryAddress').value = button.getAttribute('data-address') || '';
        document.getElementById('editDeliveryForm').action = '/menuAdministrador/delivery/' + button.getAttribute('data-id') + '/update';
    });
}

function initModalEditarRestaurante() {
    const modal = document.getElementById('editRestaurantModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        document.getElementById('editRestaurantId').value = button.getAttribute('data-id');
        document.getElementById('editRestaurantName').value = button.getAttribute('data-name');
        document.getElementById('editRestaurantLastName').value = button.getAttribute('data-lastname');
        document.getElementById('editRestaurantEmail').value = button.getAttribute('data-email');
        document.getElementById('editRestaurantPhone').value = button.getAttribute('data-phone') || '';
        document.getElementById('editRestaurantForm').action = '/menuAdministrador/restaurant/' + button.getAttribute('data-id') + '/update';
    });
}

function initModalEditarRol() {
    const modal = document.getElementById('editRolModal');
    if (!modal) return;
    
    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        
        console.log('📝 Editando rol:', button.getAttribute('data-id'), button.getAttribute('data-nombre'));
        
        document.getElementById('editRolForm').action = '/menuAdministrador/role/' + button.getAttribute('data-id') + '/update';
        document.getElementById('editRolNombre').value = button.getAttribute('data-nombre') || '';
        document.getElementById('editRolDescripcion').value = button.getAttribute('data-descripcion') || '';
        
        document.querySelectorAll('.edit-permiso-checkbox').forEach(cb => cb.checked = false);
    });
}

// ========================================
// 7. MÓDULO: FILTROS EN TIEMPO REAL
// ========================================

function initFiltros() {
    initFiltroClientes();
    initFiltroRestaurantes();
    initFiltroDelivery();
    console.log('✅ Filtros en tiempo real configurados');
}

function initFiltroClientes() {
    const nameFilter = document.getElementById('filterClienteName');
    const dniFilter = document.getElementById('filterClienteDni');
    const estadoFilter = document.getElementById('filterClienteEstado');
    
    const filterClientes = () => {
        const nameValue = nameFilter ? nameFilter.value.toLowerCase() : '';
        const dniValue = dniFilter ? dniFilter.value.toLowerCase() : '';
        const estadoValue = estadoFilter ? estadoFilter.value : '';
        
        document.querySelectorAll('#usuarios-content tbody tr').forEach(row => {
            const nombre = row.cells[1] ? row.cells[1].textContent.toLowerCase() : '';
            const dni = row.cells[2] ? row.cells[2].textContent.toLowerCase() : '';
            const estadoBadge = row.cells[4] ? row.cells[4].querySelector('.badge') : null;
            const isActivo = estadoBadge && estadoBadge.classList.contains('bg-success');
            
            let show = true;
            if (nameValue && !nombre.includes(nameValue)) show = false;
            if (dniValue && !dni.includes(dniValue)) show = false;
            if (estadoValue === 'true' && !isActivo) show = false;
            if (estadoValue === 'false' && isActivo) show = false;
            
            row.style.display = show ? '' : 'none';
        });
    };
    
    if (nameFilter) nameFilter.addEventListener('input', filterClientes);
    if (dniFilter) dniFilter.addEventListener('input', filterClientes);
    if (estadoFilter) estadoFilter.addEventListener('change', filterClientes);
}

function initFiltroRestaurantes() {
    const nameFilter = document.getElementById('filterRestaurantName');
    const rucFilter = document.getElementById('filterRestaurantRuc');
    
    const filterRestaurantes = () => {
        const nameValue = nameFilter ? nameFilter.value.toLowerCase() : '';
        const rucValue = rucFilter ? rucFilter.value : '';
        
        document.querySelectorAll('#restaurantes-content tbody tr').forEach(row => {
            const nombre = row.cells[1] ? row.cells[1].textContent.toLowerCase() : '';
            const ruc = row.cells[2] ? row.cells[2].textContent : '';
            
            let show = true;
            if (nameValue && !nombre.includes(nameValue)) show = false;
            if (rucValue && !ruc.includes(rucValue)) show = false;
            
            row.style.display = show ? '' : 'none';
        });
    };
    
    if (nameFilter) nameFilter.addEventListener('input', filterRestaurantes);
    if (rucFilter) rucFilter.addEventListener('input', filterRestaurantes);
}

function initFiltroDelivery() {
    const nameFilter = document.getElementById('filterDeliveryName');
    const emailFilter = document.getElementById('filterDeliveryEmail');
    const estadoFilter = document.getElementById('filterDeliveryEstado');
    
    const filterDelivery = () => {
        const nameValue = nameFilter ? nameFilter.value.toLowerCase() : '';
        const emailValue = emailFilter ? emailFilter.value.toLowerCase() : '';
        const estadoValue = estadoFilter ? estadoFilter.value : '';
        
        document.querySelectorAll('#delivery-content tbody tr').forEach(row => {
            const nombre = row.cells[1] ? row.cells[1].textContent.toLowerCase() : '';
            const email = row.cells[2] ? row.cells[2].textContent.toLowerCase() : '';
            const estadoBadge = row.cells[4] ? row.cells[4].querySelector('.badge') : null;
            
            let show = true;
            if (nameValue && !nombre.includes(nameValue)) show = false;
            if (emailValue && !email.includes(emailValue)) show = false;
            
            if (estadoValue && estadoBadge) {
                const isPendiente = estadoBadge.classList.contains('bg-warning');
                const isAprobado = estadoBadge.classList.contains('bg-success');
                const isRechazado = estadoBadge.classList.contains('bg-danger');
                
                if (estadoValue === 'PENDING' && !isPendiente) show = false;
                if (estadoValue === 'APPROVED' && !isAprobado) show = false;
                if (estadoValue === 'REJECTED' && !isRechazado) show = false;
            }
            
            row.style.display = show ? '' : 'none';
        });
    };
    
    if (nameFilter) nameFilter.addEventListener('input', filterDelivery);
    if (emailFilter) emailFilter.addEventListener('input', filterDelivery);
    if (estadoFilter) estadoFilter.addEventListener('change', filterDelivery);
}

// ========================================
// 8. MÓDULO: CONTRASEÑAS Y VALIDACIONES
// ========================================

function initPasswordToggles() {
    window.togglePasswordVisibility = function(inputId, button) {
        const input = document.getElementById(inputId);
        const icon = button.querySelector('i');
        
        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    };
}

function initValidaciones() {
    const adminPasswordForm = document.querySelector('form[action*="create-admin"]');
    if (!adminPasswordForm) return;
    
    adminPasswordForm.addEventListener('submit', function(e) {
        const password = document.getElementById('adminPassword').value;
        const passwordConfirm = document.getElementById('adminPasswordConfirm').value;
        
        if (password !== passwordConfirm) {
            e.preventDefault();
            alert('❌ Las contraseñas no coinciden. Por favor, verifica e intenta nuevamente.');
            document.getElementById('adminPasswordConfirm').focus();
            return false;
        }
    });
}

// ========================================
// 9. MÓDULO: SECCIÓN ACTIVA
// ========================================

function initSeccionActiva() {
    // La variable activeSection viene de Thymeleaf inline
    // Se maneja en el HTML con th:inline="javascript"
    
    const firstVisibleLink = document.querySelector('.nav-link[data-section]');
    if (firstVisibleLink) {
        const firstSection = firstVisibleLink.getAttribute('data-section');
        console.log('✅ Activando primera sección visible:', firstSection);
        firstVisibleLink.classList.add('active');
        showSection(firstSection);
    }
}

// ========================================
// 10. MÓDULO: MODAL DE CAMBIO DE ESTADO
// ========================================

function initToggleUserStatusModal() {
    const confirmButton = document.getElementById('confirmToggleUserStatus');
    if (!confirmButton) return;
    
    confirmButton.addEventListener('click', function() {
        if (pendingUserToggle) {
            const { userId, currentStatus, buttonElement } = pendingUserToggle;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('toggleUserStatusModal'));
            modal.hide();
            
            // Ejecutar el cambio de estado mediante AJAX
            toggleStatusViaAjax(`/menuAdministrador/user/${userId}/toggle-status`, buttonElement, currentStatus);
            
            // Limpiar la variable
            pendingUserToggle = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('toggleUserStatusModal').addEventListener('hidden.bs.modal', function() {
        pendingUserToggle = null;
    });
    
    console.log('✅ Modal de cambio de estado de usuario configurado');
}

function initToggleRestaurantStatusModal() {
    const confirmButton = document.getElementById('confirmToggleRestaurantStatus');
    if (!confirmButton) return;
    
    confirmButton.addEventListener('click', function() {
        if (pendingRestaurantToggle) {
            const { restaurantId, currentStatus, buttonElement } = pendingRestaurantToggle;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('toggleRestaurantStatusModal'));
            modal.hide();
            
            // Ejecutar el cambio de estado mediante AJAX
            toggleStatusViaAjax(`/menuAdministrador/restaurant/${restaurantId}/toggle-status`, buttonElement, currentStatus);
            
            // Limpiar la variable
            pendingRestaurantToggle = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('toggleRestaurantStatusModal').addEventListener('hidden.bs.modal', function() {
        pendingRestaurantToggle = null;
    });
    
    console.log('✅ Modal de cambio de estado de restaurante configurado');
}

function initToggleDeliveryAvailabilityModal() {
    const confirmButton = document.getElementById('confirmToggleDeliveryAvailability');
    if (!confirmButton) return;
    
    confirmButton.addEventListener('click', function() {
        if (pendingDeliveryAvailabilityToggle) {
            const { deliveryId, currentStatus, buttonElement } = pendingDeliveryAvailabilityToggle;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('toggleDeliveryAvailabilityModal'));
            modal.hide();
            
            // Ejecutar el cambio de estado mediante AJAX
            toggleStatusViaAjax(`/menuAdministrador/repartidor/${deliveryId}/toggle-disponibilidad`, buttonElement, currentStatus);
            
            // Limpiar la variable
            pendingDeliveryAvailabilityToggle = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('toggleDeliveryAvailabilityModal').addEventListener('hidden.bs.modal', function() {
        pendingDeliveryAvailabilityToggle = null;
    });
    
    console.log('✅ Modal de cambio de disponibilidad de repartidor configurado');
}

function initToggleDeliveryStatusModal() {
    const confirmButton = document.getElementById('confirmToggleDeliveryStatus');
    if (!confirmButton) return;
    
    confirmButton.addEventListener('click', function() {
        if (pendingDeliveryStatusToggle) {
            const { deliveryId, currentStatus, buttonElement } = pendingDeliveryStatusToggle;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('toggleDeliveryStatusModal'));
            modal.hide();
            
            // Ejecutar el cambio de estado mediante AJAX
            toggleStatusViaAjax(`/menuAdministrador/repartidor/${deliveryId}/toggle-status`, buttonElement, currentStatus);
            
            // Limpiar la variable
            pendingDeliveryStatusToggle = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('toggleDeliveryStatusModal').addEventListener('hidden.bs.modal', function() {
        pendingDeliveryStatusToggle = null;
    });
    
    console.log('✅ Modal de cambio de estado de repartidor configurado');
}

function initToggleCategoryStatusModal() {
    const confirmButton = document.getElementById('confirmToggleCategoryStatus');
    if (!confirmButton) return;
    
    confirmButton.addEventListener('click', function() {
        if (pendingCategoryToggle) {
            const { categoryId, currentStatus, buttonElement } = pendingCategoryToggle;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('toggleCategoryStatusModal'));
            modal.hide();
            
            // Ejecutar el cambio de estado mediante AJAX
            toggleStatusViaAjax(`/menuAdministrador/category/${categoryId}/toggle-status`, buttonElement, currentStatus);
            
            // Limpiar la variable
            pendingCategoryToggle = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('toggleCategoryStatusModal').addEventListener('hidden.bs.modal', function() {
        pendingCategoryToggle = null;
    });
    
    console.log('✅ Modal de cambio de estado de categoría configurado');
}

function initDeleteCategoryModal() {
    const confirmButton = document.getElementById('confirmDeleteCategory');
    if (!confirmButton) return;
    
    confirmButton.addEventListener('click', function() {
        if (pendingCategoryDelete) {
            const { categoryId } = pendingCategoryDelete;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('deleteCategoryModal'));
            modal.hide();
            
            // Ejecutar eliminación mediante POST con formulario
            submitFormWithJWT(`/menuAdministrador/category/${categoryId}/delete`, 'configuracion');
            
            // Limpiar la variable
            pendingCategoryDelete = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('deleteCategoryModal').addEventListener('hidden.bs.modal', function() {
        pendingCategoryDelete = null;
    });
    
    console.log('✅ Modal de eliminación de categoría configurado');
}

