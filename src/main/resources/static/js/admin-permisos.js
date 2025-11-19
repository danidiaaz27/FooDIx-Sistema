/**
 * ========================================
 * FOODIX - GESTIÓN DE PERMISOS
 * Sistema completo de permisos para roles
 * ========================================
 */

// Variable global para almacenar todos los permisos
window.todosLosPermisos = [];

// ========================================
// 1. INICIALIZACIÓN DE PERMISOS
// ========================================

function initPermisos() {
    console.log('🚀 Inicializando sistema de permisos...');
    
    // Los permisos se cargan desde Thymeleaf inline en el HTML
    // Ver: <script th:inline="javascript"> en menuAdministrador.html
    
    initGestionPermisos();
    initGestionPermisosEdicion();
    initModalPermisos();
    initVisualizacionPermisos();
    
    console.log('✅ Sistema de permisos inicializado');
}

// ========================================
// 2. GESTIÓN DE PERMISOS (CREAR ROL)
// ========================================

function initGestionPermisos() {
    // Checkboxes de permisos
    document.querySelectorAll('.permiso-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', updatePermisosDisplay);
    });
    
    // Seleccionar todos los permisos de una sección
    document.querySelectorAll('.select-all-section').forEach(selectAll => {
        selectAll.addEventListener('change', function() {
            const section = this.dataset.section;
            const checkboxes = document.querySelectorAll(`.permiso-checkbox[data-section="${section}"]`);
            checkboxes.forEach(cb => cb.checked = this.checked);
            updatePermisosDisplay();
        });
    });
    
    // Confirmar selección
    document.getElementById('confirmPermisos')?.addEventListener('click', function() {
        updatePermisosDisplay();
        updateHiddenInputs();
    });
    
    // Inicializar display
    updatePermisosDisplay();
}

function updatePermisosDisplay() {
    const selectedCheckboxes = document.querySelectorAll('.permiso-checkbox:checked');
    const count = selectedCheckboxes.length;
    const countBadge = document.getElementById('countPermisos');
    const displayArea = document.getElementById('selectedPermisosDisplay');
    
    if (countBadge) {
        countBadge.textContent = count;
    }
    
    if (displayArea) {
        if (count === 0) {
            displayArea.innerHTML = '<small class="text-muted">No hay permisos seleccionados</small>';
        } else {
            let html = '<div class="d-flex flex-wrap gap-1">';
            selectedCheckboxes.forEach(cb => {
                const accion = cb.dataset.accion || 'Permiso';
                const badgeColor = getBadgeColorForSection(cb.dataset.section);
                html += `<span class="badge ${badgeColor}" style="font-size: 0.75rem;">${accion}</span>`;
            });
            html += '</div>';
            displayArea.innerHTML = html;
        }
    }
}

function updateHiddenInputs() {
    const container = document.getElementById('hiddenPermisosContainer');
    if (!container) return;
    
    container.innerHTML = '';
    
    document.querySelectorAll('.permiso-checkbox:checked').forEach(cb => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'permisos';
        input.value = cb.value;
        container.appendChild(input);
    });
}

// ========================================
// 3. GESTIÓN DE PERMISOS (EDITAR ROL)
// ========================================

function initGestionPermisosEdicion() {
    // Checkboxes de edición
    document.querySelectorAll('.edit-permiso-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', updateEditPermisosDisplay);
    });
    
    // Seleccionar todos de una sección
    document.querySelectorAll('.edit-select-all-section').forEach(selectAll => {
        selectAll.addEventListener('change', function() {
            const section = this.dataset.section;
            const checkboxes = document.querySelectorAll(`.edit-permiso-checkbox[data-section="${section}"]`);
            checkboxes.forEach(cb => cb.checked = this.checked);
            updateEditPermisosDisplay();
        });
    });
    
    // Confirmar selección de edición
    document.getElementById('confirmEditPermisos')?.addEventListener('click', function() {
        updateEditPermisosDisplay();
        updateEditHiddenInputs();
    });
    
    // Inicializar
    updateEditPermisosDisplay();
}

function updateEditPermisosDisplay() {
    const selectedCheckboxes = document.querySelectorAll('.edit-permiso-checkbox:checked');
    const count = selectedCheckboxes.length;
    const countBadge = document.getElementById('editCountPermisos');
    const displayArea = document.getElementById('editSelectedPermisosDisplay');
    
    if (countBadge) {
        countBadge.textContent = count;
    }
    
    if (displayArea) {
        if (count === 0) {
            displayArea.innerHTML = '<small class="text-muted">No hay permisos seleccionados</small>';
        } else {
            let html = '<div class="d-flex flex-wrap gap-1">';
            selectedCheckboxes.forEach(cb => {
                const accion = cb.dataset.accion || 'Permiso';
                const badgeColor = getBadgeColorForSection(cb.dataset.section);
                html += `<span class="badge ${badgeColor}" style="font-size: 0.75rem;">${accion}</span>`;
            });
            html += '</div>';
            displayArea.innerHTML = html;
        }
    }
}

function updateEditHiddenInputs() {
    const container = document.getElementById('editHiddenPermisosContainer');
    if (!container) return;
    
    container.innerHTML = '';
    
    document.querySelectorAll('.edit-permiso-checkbox:checked').forEach(cb => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'permisos';
        input.value = cb.value;
        container.appendChild(input);
    });
}

// ========================================
// 4. CARGAR PERMISOS DE UN ROL
// ========================================

window.loadRolPermisos = function(rolId) {
    console.log('🔍 Cargando permisos del rol:', rolId);
    
    fetch('/menuAdministrador/role/' + rolId + '/permisos')
        .then(response => response.json())
        .then(permisos => {
            console.log('✅ Permisos cargados:', permisos);
            
            document.querySelectorAll('.edit-permiso-checkbox').forEach(checkbox => {
                const permisoCodigo = parseInt(checkbox.getAttribute('data-codigo'));
                checkbox.checked = permisos.includes(permisoCodigo);
            });
        })
        .catch(error => {
            console.error('❌ Error cargando permisos:', error);
        });
};

// ========================================
// 5. MODAL DE SELECCIÓN DE PERMISOS
// ========================================

function initModalPermisos() {
    // Funciones para abrir/cerrar modal manualmente
    window.abrirModalSeleccionPermisos = function() {
        console.log('📋 Abriendo modal de selección de permisos...');
        
        const modalElement = document.getElementById('selectPermisosModal');
        if (!modalElement) {
            console.error('❌ Modal selectPermisosModal no encontrado');
            return;
        }
        
        modalElement.style.display = 'block';
        modalElement.style.paddingRight = '0px';
        modalElement.classList.add('show');
        modalElement.setAttribute('aria-modal', 'true');
        modalElement.removeAttribute('aria-hidden');
        
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-backdrop fade show';
        backdrop.id = 'selectPermisosBackdrop';
        document.body.appendChild(backdrop);
        
        document.body.classList.add('modal-open');
        document.body.style.overflow = 'hidden';
        
        console.log('✅ Modal de selección abierto');
    };
    
    window.cerrarModalSeleccionPermisos = function() {
        console.log('🚪 Cerrando modal de selección...');
        
        const modalElement = document.getElementById('selectPermisosModal');
        if (modalElement) {
            modalElement.style.display = 'none';
            modalElement.classList.remove('show');
            modalElement.setAttribute('aria-hidden', 'true');
            modalElement.removeAttribute('aria-modal');
        }
        
        const backdrop = document.getElementById('selectPermisosBackdrop');
        if (backdrop) backdrop.remove();
        
        document.body.classList.remove('modal-open');
        document.body.style.overflow = '';
        document.body.style.paddingRight = '';
        
        console.log('✅ Modal cerrado');
    };
}

// ========================================
// 6. VISUALIZACIÓN DE PERMISOS DE UN ROL
// ========================================

function initVisualizacionPermisos() {
    // Función para abrir modal manualmente
    window.verPermisosRolManual = function(button) {
        console.log('🔘 Botón clickeado, abriendo modal manualmente...');
        
        const rolId = parseInt(button.getAttribute('data-rol-id'));
        const rolNombre = button.getAttribute('data-rol-nombre');
        
        console.log('📌 Rol ID:', rolId, 'Nombre:', rolNombre);
        
        const modalElement = document.getElementById('viewPermisosModal');
        if (!modalElement) {
            console.error('❌ Modal no encontrado');
            return;
        }
        
        // Cargar los permisos
        showRolPermisos(rolId, rolNombre);
        
        // Abrir modal manualmente
        console.log('🎭 Abriendo modal...');
        
        modalElement.style.display = 'block';
        modalElement.style.paddingRight = '0px';
        modalElement.classList.add('show');
        modalElement.setAttribute('aria-modal', 'true');
        modalElement.removeAttribute('aria-hidden');
        
        const backdrop = document.createElement('div');
        backdrop.className = 'modal-backdrop fade show';
        backdrop.id = 'customBackdrop';
        document.body.appendChild(backdrop);
        
        document.body.classList.add('modal-open');
        document.body.style.overflow = 'hidden';
        document.body.style.paddingRight = '0px';
        
        console.log('✅ Modal abierto manualmente');
    };
    
    // Función para cerrar modal
    window.cerrarModalPermisos = function() {
        console.log('🚪 Cerrando modal...');
        
        const modalElement = document.getElementById('viewPermisosModal');
        if (modalElement) {
            modalElement.style.display = 'none';
            modalElement.classList.remove('show');
            modalElement.setAttribute('aria-hidden', 'true');
            modalElement.removeAttribute('aria-modal');
        }
        
        const backdrop = document.getElementById('customBackdrop');
        if (backdrop) backdrop.remove();
        
        document.body.classList.remove('modal-open');
        document.body.style.overflow = '';
        document.body.style.paddingRight = '';
        
        console.log('✅ Modal cerrado');
    };
    
    // Event listeners para botones de cerrar
    setTimeout(() => {
        const closeButtons = document.querySelectorAll('#viewPermisosModal [data-bs-dismiss="modal"]');
        closeButtons.forEach(btn => {
            btn.addEventListener('click', cerrarModalPermisos);
        });
        
        document.addEventListener('click', function(e) {
            if (e.target && e.target.id === 'customBackdrop') {
                cerrarModalPermisos();
            }
        });
    }, 500);
    
    // Botón de editar desde modal de visualización
    document.getElementById('editPermisosFromView')?.addEventListener('click', function() {
        const rolId = this.getAttribute('data-rol-id');
        console.log('🔧 Editando permisos del rol:', rolId);
        
        cerrarModalPermisos();
        
        setTimeout(() => {
            const editBtn = document.querySelector(`button[data-bs-target="#editRolModal"][data-id="${rolId}"]`);
            if (editBtn) {
                console.log('✅ Abriendo modal de edición');
                editBtn.click();
            } else {
                console.error('❌ No se encontró el botón de editar para el rol:', rolId);
            }
        }, 300);
    });
}

// ========================================
// 7. MOSTRAR PERMISOS DE UN ROL (AJAX)
// ========================================

window.showRolPermisos = function(rolId, rolNombre) {
    console.log('👁️ Mostrando permisos del rol:', rolId, rolNombre);
    
    const titleElement = document.getElementById('viewRolNombre');
    const contentElement = document.getElementById('permisosContent');
    const editButton = document.getElementById('editPermisosFromView');
    
    if (!titleElement || !contentElement) {
        console.error('❌ Elementos del modal no encontrados');
        return;
    }
    
    titleElement.textContent = rolNombre;
    
    if (editButton) {
        editButton.setAttribute('data-rol-id', rolId);
        editButton.setAttribute('data-rol-nombre', rolNombre);
    }
    
    contentElement.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Cargando...</span></div></div>';
    
    fetch('/menuAdministrador/role/' + rolId + '/permisos')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor: ' + response.status);
            }
            return response.json();
        })
        .then(permisosIds => {
            console.log('✅ Permisos del rol cargados:', permisosIds);
            
            if (!Array.isArray(permisosIds)) {
                throw new Error('Respuesta inválida del servidor');
            }
            
            if (!window.todosLosPermisos || window.todosLosPermisos.length === 0) {
                console.error('❌ No hay permisos globales disponibles');
                contentElement.innerHTML = '<div class="col-12"><div class="alert alert-warning"><i class="fas fa-exclamation-triangle me-2"></i>No se pudieron cargar los permisos disponibles</div></div>';
                return;
            }
            
            // Agrupar por sección
            const permisosPorSeccion = {};
            window.todosLosPermisos.forEach(permiso => {
                if (permisosIds.includes(permiso.codigo)) {
                    if (!permisosPorSeccion[permiso.seccion]) {
                        permisosPorSeccion[permiso.seccion] = [];
                    }
                    permisosPorSeccion[permiso.seccion].push(permiso);
                }
            });
            
            console.log('📦 Permisos agrupados por sección:', Object.keys(permisosPorSeccion));
            
            // Construir HTML
            let html = '';
            const secciones = [
                {name: 'usuarios', icon: 'fa-users-cog', label: 'Usuarios', color: 'info'},
                {name: 'clientes', icon: 'fa-user-friends', label: 'Clientes', color: 'success'},
                {name: 'restaurantes', icon: 'fa-utensils', label: 'Restaurantes', color: 'warning'},
                {name: 'delivery', icon: 'fa-motorcycle', label: 'Repartidores', color: 'danger'},
                {name: 'categorias', icon: 'fa-tags', label: 'Categorías', color: 'secondary'},
                {name: 'configuracion', icon: 'fa-cogs', label: 'Configuración', color: 'dark'}
            ];
            
            secciones.forEach(seccion => {
                const permisos = permisosPorSeccion[seccion.name];
                if (permisos && permisos.length > 0) {
                    html += `
                        <div class="col-md-6">
                            <div class="card border-${seccion.color} mb-3">
                                <div class="card-header bg-${seccion.color} bg-opacity-10">
                                    <h6 class="mb-0 text-${seccion.color}">
                                        <i class="fas ${seccion.icon} me-2"></i>${seccion.label}
                                    </h6>
                                </div>
                                <div class="card-body">
                                    <ul class="list-unstyled mb-0">`;
                    
                    permisos.forEach(p => {
                        const nombrePermiso = p.nombre || p.accion || 'Permiso';
                        const descripcion = p.descripcion || '';
                        html += `
                            <li class="mb-2">
                                <i class="fas fa-check-circle text-${seccion.color} me-2"></i>
                                <span class="fw-bold">${nombrePermiso}</span>
                                ${descripcion ? '<br><small class="text-muted ms-4">' + descripcion + '</small>' : ''}
                            </li>`;
                    });
                    
                    html += `
                                    </ul>
                                </div>
                            </div>
                        </div>`;
                }
            });
            
            if (html === '') {
                console.warn('⚠️ HTML vacío, no hay permisos para mostrar');
                html = '<div class="col-12"><div class="alert alert-warning"><i class="fas fa-exclamation-triangle me-2"></i>Este rol no tiene permisos asignados</div></div>';
            }
            
            console.log('✅ HTML construido, actualizando DOM...');
            contentElement.innerHTML = html;
            console.log('✅✅ Permisos mostrados en el modal');
        })
        .catch(error => {
            console.error('❌ Error cargando permisos:', error);
            contentElement.innerHTML = 
                '<div class="col-12"><div class="alert alert-danger"><i class="fas fa-times-circle me-2"></i>Error al cargar los permisos: ' + error.message + '</div></div>';
        });
};

// ========================================
// 8. UTILIDADES
// ========================================

function getBadgeColorForSection(section) {
    const colors = {
        'usuarios': 'bg-info',
        'clientes': 'bg-success',
        'restaurantes': 'bg-warning text-dark',
        'delivery': 'bg-danger',
        'categorias': 'bg-secondary',
        'configuracion': 'bg-dark'
    };
    return colors[section] || 'bg-primary';
}
