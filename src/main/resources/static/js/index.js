// =============================================
// INDEX.JS - Lógica para página principal
// =============================================

document.addEventListener('DOMContentLoaded', function() {
    console.log('FoodIx - Sistema cargado correctamente');
    
    // Auto-cierre de alertas después de 5 segundos
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            if (alert && alert.parentNode) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000);
    });
});

// =============================================
// FUNCIONES PARA MODAL DE NEGOCIOS
// =============================================

/**
 * Selecciona el tipo de negocio (restaurante o repartidor)
 * y redirige a verificacion.html con tipo=negocio
 */
function selectBusinessType(type) {
    console.log('📌 Tipo de negocio seleccionado:', type);
    
    // Guardar en localStorage para uso posterior
    localStorage.setItem('selectedBusinessType', type);
    console.log('💾 Guardado en localStorage:', type);
    
    // Redirigir a verificación con tipo=negocio
    window.location.href = '/verificacion?tipo=negocio';
}

/**
 * Redirige a la página de verificación de correo
 */
function redirectToVerification() {
    console.log('🔄 Redirigiendo a verificación...');
    
    // Obtener el tipo seleccionado
    const businessType = localStorage.getItem('selectedBusinessType');
    
    // Siempre redirigir con tipo=negocio si hay un tipo guardado
    if (businessType) {
        window.location.href = '/verificacion?tipo=negocio';
    } else {
        window.location.href = '/verificacion?tipo=usuario';
    }
}

// =============================================
// ESTILOS ADICIONALES PARA HOVER CARDS
// =============================================

// Agregar estilos dinámicamente
const style = document.createElement('style');
style.textContent = `
    .hover-card {
        transition: all 0.3s ease;
    }
    
    .hover-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 20px rgba(0,0,0,0.2) !important;
    }
    
    .modal-content {
        border-radius: 15px;
        overflow: hidden;
    }
    
    .modal-header.bg-gradient {
        border: none;
    }
    
    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .modal.show .modal-dialog {
        animation: fadeInUp 0.3s ease-out;
    }
`;
document.head.appendChild(style);
