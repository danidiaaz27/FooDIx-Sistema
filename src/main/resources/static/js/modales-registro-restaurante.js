/**
 * modales-registro-restaurante.js
 * Gestión de modales informativos según documentos cargados en registro de restaurante
 */

// Configuración de mensajes según cantidad de archivos
const MODAL_MESSAGES = {
    0: {
        icon: '⚠️',
        title: 'Verificación Pendiente',
        status: {
            accountStatus: 'Registrada ✅',
            promotionsStatus: 'Inactivo ❌'
        },
        message: 'Has completado tu registro exitosamente, pero para habilitar la función de publicar promociones debes subir los documentos requeridos para verificación.',
        nextSteps: [
            'Accede a tu perfil de usuario',
            'Sube los archivos solicitados',
            'Nuestro equipo los revisará pronto'
        ],
        footer: '¡Te notificaremos cuando todo esté listo!',
        variant: 'warning'
    },
    1: {
        icon: '🚀',
        title: '¡Vas por buen camino!',
        message: 'Has comenzado el proceso de verificación subiendo <strong>1 documento</strong>. Solo te faltan <strong>2 más</strong> para activar tu cuenta completa.',
        footer: '¡Completa la carga y podrás publicar tus promociones!',
        variant: 'info'
    },
    2: {
        icon: '🚀',
        title: '¡Vas por buen camino!',
        message: 'Has comenzado el proceso de verificación subiendo <strong>2 documentos</strong>. Solo te falta <strong>1 más</strong> para activar tu cuenta completa.',
        footer: '¡Completa la carga y podrás publicar tus promociones!',
        variant: 'info'
    },
    3: {
        icon: '🎯',
        title: '¡Todo Listo por tu Parte!',
        progress: '100% de la carga de documentos (3/3)',
        message: 'Has completado el <strong>100%</strong> de la carga de documentos (3/3).',
        reviewSection: {
            title: 'Ahora estamos nosotros:',
            message: 'Nuestro equipo está verificando tu información. Esta revisión manual requiere un poco de tiempo.'
        },
        notification: '📬 <strong>Te avisaremos inmediatamente</strong> cuando todo esté aprobado y puedas publicar tus promociones.',
        footer: '¡Estás a un paso de empezar!',
        variant: 'success'
    }
};

/**
 * Cuenta los archivos cargados en el formulario
 * @returns {number} Cantidad de archivos seleccionados (0-3)
 */
function countUploadedFiles() {
    const fileInputs = [
        document.getElementById('Carta'),
        document.getElementById('CarnetSanidad'),
        document.getElementById('LicenciaFuncionamiento')
    ];
    
    return fileInputs.filter(input => input && input.files && input.files.length > 0).length;
}

/**
 * Muestra el modal correspondiente según la cantidad de archivos
 * @param {number} filesCount - Cantidad de archivos cargados
 */
function showVerificationModal(filesCount) {
    const config = MODAL_MESSAGES[filesCount];
    
    if (!config) {
        console.error('Configuración de modal no encontrada para:', filesCount);
        return;
    }
    
    // Crear o actualizar el modal
    createOrUpdateModal(config);
    
    // Mostrar el modal
    const modalElement = document.getElementById('verificationModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
}

/**
 * Crea o actualiza el contenido del modal dinámicamente
 * @param {Object} config - Configuración del mensaje del modal
 */
function createOrUpdateModal(config) {
    let modalElement = document.getElementById('verificationModal');
    
    // Si no existe, crear el modal
    if (!modalElement) {
        modalElement = document.createElement('div');
        modalElement.id = 'verificationModal';
        modalElement.className = 'modal fade';
        modalElement.setAttribute('tabindex', '-1');
        modalElement.setAttribute('aria-labelledby', 'verificationModalLabel');
        modalElement.setAttribute('aria-hidden', 'true');
        document.body.appendChild(modalElement);
    }
    
    // Construir el contenido del modal
    const headerClass = getHeaderClass(config.variant);
    const iconHtml = `<span class="modal-icon">${config.icon}</span>`;
    
    let bodyContent = '';
    
    // Caso 0 archivos: Mostrar estado de cuenta
    if (config.status) {
        bodyContent += `
            <div class="status-section mb-3">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="text-muted"><strong>Estado de tu cuenta:</strong></span>
                    <span class="badge bg-success">${config.status.accountStatus}</span>
                </div>
                <div class="d-flex justify-content-between align-items-center">
                    <span class="text-muted"><strong>Publicar promociones:</strong></span>
                    <span class="badge bg-danger">${config.status.promotionsStatus}</span>
                </div>
            </div>
            <hr>
        `;
    }
    
    // Progreso (solo para 3 archivos)
    if (config.progress) {
        bodyContent += `
            <div class="progress-section mb-3">
                <div class="progress" style="height: 25px;">
                    <div class="progress-bar bg-success progress-bar-striped progress-bar-animated" 
                         role="progressbar" style="width: 100%" 
                         aria-valuenow="100" aria-valuemin="0" aria-valuemax="100">
                        ${config.progress}
                    </div>
                </div>
            </div>
        `;
    }
    
    // Mensaje principal
    bodyContent += `<p class="mb-3">${config.message}</p>`;
    
    // Sección de revisión (solo para 3 archivos)
    if (config.reviewSection) {
        bodyContent += `
            <div class="alert alert-info mb-3">
                <h6 class="alert-heading mb-2"><strong>${config.reviewSection.title}</strong></h6>
                <p class="mb-0">${config.reviewSection.message}</p>
            </div>
        `;
    }
    
    // Notificación especial
    if (config.notification) {
        bodyContent += `<p class="text-center mb-3">${config.notification}</p>`;
    }
    
    // Próximos pasos (solo para 0 archivos)
    if (config.nextSteps) {
        bodyContent += `
            <div class="next-steps-section mb-3">
                <h6 class="mb-2"><strong>Próximos pasos:</strong></h6>
                <ul class="list-unstyled mb-0">
                    ${config.nextSteps.map(step => `
                        <li class="mb-1">
                            <i class="fas fa-check-circle text-success me-2"></i>${step}
                        </li>
                    `).join('')}
                </ul>
            </div>
        `;
    }
    
    // Footer del mensaje
    if (config.footer) {
        bodyContent += `
            <div class="alert alert-${config.variant} mt-3 mb-0">
                <p class="mb-0 text-center"><strong>${config.footer}</strong></p>
            </div>
        `;
    }
    
    // Construir HTML completo del modal
    modalElement.innerHTML = `
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content">
                <div class="modal-header ${headerClass}">
                    <h5 class="modal-title d-flex align-items-center" id="verificationModalLabel">
                        ${iconHtml}
                        <span class="ms-2">${config.title}</span>
                    </h5>
                    <button type="button" class="btn-close ${config.variant === 'warning' || config.variant === 'info' ? 'btn-close-white' : ''}" 
                            data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    ${bodyContent}
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-${config.variant === 'warning' ? 'primary' : config.variant}" data-bs-dismiss="modal">
                        <i class="fas fa-check me-2"></i>Entendido
                    </button>
                </div>
            </div>
        </div>
    `;
}

/**
 * Obtiene la clase CSS del header según la variante
 * @param {string} variant - Variante del modal (warning, info, success)
 * @returns {string} Clase CSS para el header
 */
function getHeaderClass(variant) {
    const classes = {
        'warning': 'bg-warning text-dark',
        'info': 'bg-info text-white',
        'success': 'bg-success text-white'
    };
    return classes[variant] || 'bg-primary text-white';
}

/**
 * Maneja el envío del formulario y muestra el modal apropiado
 * @param {Event} event - Evento de submit del formulario
 */
function handleRestaurantFormSubmit(event) {
    // Contar archivos antes de enviar
    const filesCount = countUploadedFiles();
    
    console.log(`📊 Archivos cargados: ${filesCount}/3`);
    
    // El formulario se enviará normalmente
    // El modal se mostrará después de la respuesta del servidor
    // Para esto, necesitamos interceptar después del submit
    
    // Guardar la cantidad de archivos en sessionStorage para mostrar el modal después
    sessionStorage.setItem('restaurantFilesCount', filesCount);
}

/**
 * Verifica si debe mostrar el modal después de un registro exitoso
 */
function checkAndShowModalAfterRegistration() {
    const filesCount = sessionStorage.getItem('restaurantFilesCount');
    
    if (filesCount !== null) {
        // Remover del sessionStorage
        sessionStorage.removeItem('restaurantFilesCount');
        
        // Mostrar el modal con un pequeño delay para mejor UX
        setTimeout(() => {
            showVerificationModal(parseInt(filesCount));
        }, 500);
    }
}

/**
 * Inicialización al cargar el DOM
 */
document.addEventListener('DOMContentLoaded', function() {
    // Agregar listener al formulario de restaurante
    const restaurantForm = document.getElementById('restaurantForm');
    if (restaurantForm) {
        restaurantForm.addEventListener('submit', handleRestaurantFormSubmit);
    }
    
    // Verificar si debe mostrar modal después de registro
    checkAndShowModalAfterRegistration();
    
    // Agregar estilos personalizados para los modales
    addCustomModalStyles();
});

/**
 * Agrega estilos CSS personalizados para los modales
 */
function addCustomModalStyles() {
    if (document.getElementById('modal-custom-styles')) {
        return; // Ya existen los estilos
    }
    
    const styleSheet = document.createElement('style');
    styleSheet.id = 'modal-custom-styles';
    styleSheet.textContent = `
        .modal-icon {
            font-size: 1.5rem;
        }
        
        .status-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
        }
        
        .progress-section .progress {
            border-radius: 15px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .next-steps-section ul li {
            padding: 5px 0;
        }
        
        .modal-header.bg-warning,
        .modal-header.bg-info,
        .modal-header.bg-success {
            border-bottom: none;
        }
        
        .modal-body p:last-child {
            margin-bottom: 0;
        }
        
        .alert-heading {
            font-size: 1rem;
        }
        
        .badge {
            font-size: 0.875rem;
            padding: 0.35em 0.65em;
        }
    `;
    
    document.head.appendChild(styleSheet);
}

/**
 * API pública para mostrar modal manualmente (útil para testing)
 * @param {number} filesCount - Cantidad de archivos (0-3)
 */
window.showRestaurantVerificationModal = showVerificationModal;
