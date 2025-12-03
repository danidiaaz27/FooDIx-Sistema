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
        submitFormWithCSRF(`/menuAdministrador/restaurant/${id}/approve`, 'restaurantes');
    }
}

function toggleEstadoRestaurante(button) {
    const id = button.dataset.id;
    const estadoActual = button.dataset.estado === 'true';
    const nuevoEstado = estadoActual ? 'desactivar' : 'activar';
    
    if (confirm(`¿Está seguro de ${nuevoEstado} este restaurante?`)) {
        submitFormWithCSRF(`/menuAdministrador/restaurant/${id}/toggle-status`);
    }
}

function eliminarRestaurante(button) {
    const id = button.dataset.id;
    const nombre = button.dataset.nombre;
    
    if (confirm(`⚠️ ADVERTENCIA ⚠️\n\n¿Está seguro de ELIMINAR el restaurante "${nombre}"?\n\nEsta acción NO se puede deshacer.`)) {
        submitFormWithCSRF(`/menuAdministrador/restaurant/${id}/delete`);
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

function toggleUserStatus(userId, currentStatus) {
    const modal = document.getElementById('toggleUserStatusModal');
    const bootstrapModal = new bootstrap.Modal(modal);
    
    // Guardar información para usarla al confirmar
    pendingUserToggle = { userId, currentStatus };
    
    // Configurar textos según el estado actual
    const action = currentStatus ? 'desactivar' : 'activar';
    const statusText = currentStatus ? 'desactivado' : 'activado';
    const iconClass = currentStatus ? 'fa-user-slash text-danger' : 'fa-user-check text-success';
    const actionClass = currentStatus ? 'text-danger' : 'text-success';
    
    // Actualizar contenido del modal
    document.getElementById('statusModalIcon').className = `fas ${iconClass} fa-3x`;
    document.getElementById('statusModalAction').textContent = action;
    document.getElementById('statusModalAction').className = `fw-bold ${actionClass}`;
    document.getElementById('statusModalResult').textContent = statusText;
    
    // Mostrar el modal
    bootstrapModal.show();
}

function submitFormWithCSRF(action, section = null) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = action;
    
    // Token CSRF
    const csrfToken = document.querySelector('meta[name="_csrf"]');
    if (csrfToken) {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = '_csrf';
        input.value = csrfToken.getAttribute('content');
        form.appendChild(input);
    }
    
    // Sección activa
    if (section) {
        const sectionInput = document.createElement('input');
        sectionInput.type = 'hidden';
        sectionInput.name = 'section';
        sectionInput.value = section;
        form.appendChild(sectionInput);
    }
    
    document.body.appendChild(form);
    form.submit();
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
            const { userId, currentStatus } = pendingUserToggle;
            
            // Cerrar el modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('toggleUserStatusModal'));
            modal.hide();
            
            // Ejecutar el cambio de estado
            submitFormWithCSRF(`/menuAdministrador/user/${userId}/toggle-status`);
            
            // Limpiar la variable
            pendingUserToggle = null;
        }
    });
    
    // Limpiar al cerrar el modal sin confirmar
    document.getElementById('toggleUserStatusModal').addEventListener('hidden.bs.modal', function() {
        pendingUserToggle = null;
    });
    
    console.log('✅ Modal de cambio de estado configurado');
}
