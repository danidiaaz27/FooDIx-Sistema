// Función para mostrar el formulario de correo
function showEmailForm() {
    document.getElementById('emailForm').style.display = 'block';
    document.getElementById('verificationForm').style.display = 'none';
}

// Función para mostrar el formulario de verificación
function showVerificationForm() {
    document.getElementById('emailForm').style.display = 'none';
    document.getElementById('verificationForm').style.display = 'block';
    // Inicializar inputs del código después de mostrar el formulario
    setTimeout(() => {
        initCodeInputs();
    }, 100);
}

// Función para mostrar/ocultar el modal
function showModal() {
    const modal = document.getElementById('codeModal');
    modal.classList.add('show');
}

function hideModal() {
    const modal = document.getElementById('codeModal');
    modal.classList.remove('show');
}

// Manejar el envío del formulario de correo
document.getElementById('emailForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const email = document.getElementById('email').value;
    const tipo = document.getElementById('tipoRegistro').value || 'usuario';
    const submitBtn = this.querySelector('button[type="submit"]');
    
    // Validar email
    if (!email || !isValidEmail(email)) {
        showAlert('Por favor ingresa un correo electrónico válido', 'danger');
        return;
    }
    
    // Mostrar el modal con la animación
    showModal();
    
    // Deshabilitar botón
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Enviando...';
    
    try {
        // Llamada REAL al backend
        const formData = new URLSearchParams();
        formData.append('email', email);
        formData.append('tipo', tipo); // Agregar el tipo
        
        console.log('📧 Enviando código a:', email, 'Tipo:', tipo);
        
        const response = await fetch('/auth/send-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        });
        
        console.log('📬 Respuesta recibida:', response.status);
        
        const result = await response.text();
        console.log('📄 Resultado:', result);
        
        const data = JSON.parse(result);
        
        if (data.success) {
            // Éxito: mostrar formulario de verificación
            hideModal();
            showVerificationForm();
            showAlert('Código enviado exitosamente. Revisa tu correo.', 'success');
        } else {
            // Error: mostrar mensaje
            hideModal();
            showAlert(data.error || 'Error al enviar el código', 'danger');
        }
        
    } catch (error) {
        console.error('❌ Error:', error);
        hideModal();
        showAlert('Error de conexión. Intenta nuevamente.', 'danger');
    } finally {
        // Rehabilitar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = '<i class="fas fa-paper-plane"></i> Enviar código';
    }
});

// Referencias a inputs del código
let codeInputs = [];
let countdownInterval = null;

// Función para mostrar alertas dinámicas
function showAlert(message, type = 'danger') {
    const alertContainer = document.getElementById('alertContainer');
    alertContainer.innerHTML = `
        <div class="alert alert-${type}">
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
            <span>${message}</span>
            <button type="button" class="btn-close" onclick="this.parentElement.remove()">×</button>
        </div>
    `;
}

// Inicializar inputs del código cuando el formulario se muestre
function initCodeInputs() {
    codeInputs = [
        document.getElementById('digit1'),
        document.getElementById('digit2'),
        document.getElementById('digit3'),
        document.getElementById('digit4'),
        document.getElementById('digit5'),
        document.getElementById('digit6')
    ];

    if (!codeInputs[0]) return; // Si no existen los inputs, salir

    // Auto-focus y navegación entre inputs
    codeInputs.forEach((input, index) => {
        input.addEventListener('input', (e) => {
            const value = e.target.value;
            
            // Solo permitir números
            if (!/^\d$/.test(value)) {
                e.target.value = '';
                return;
            }

            // Marcar como completado
            input.classList.add('filled');
            
            // Mover al siguiente input
            if (value && index < codeInputs.length - 1) {
                codeInputs[index + 1].focus();
            }

            // Actualizar campo hidden y habilitar botón
            updateCodeField();
            checkAllFilled();
        });

        input.addEventListener('keydown', (e) => {
            // Borrar: volver al input anterior
            if (e.key === 'Backspace' && !input.value && index > 0) {
                codeInputs[index - 1].focus();
                codeInputs[index - 1].value = '';
                codeInputs[index - 1].classList.remove('filled');
                updateCodeField();
                checkAllFilled();
            }

            // Flechas de navegación
            if (e.key === 'ArrowLeft' && index > 0) {
                codeInputs[index - 1].focus();
            }
            if (e.key === 'ArrowRight' && index < codeInputs.length - 1) {
                codeInputs[index + 1].focus();
            }
        });

        input.addEventListener('paste', (e) => {
            e.preventDefault();
            const pastedData = e.clipboardData.getData('text').slice(0, 6);
            const digits = pastedData.split('');
            
            digits.forEach((digit, i) => {
                if (i < codeInputs.length && /^\d$/.test(digit)) {
                    codeInputs[i].value = digit;
                    codeInputs[i].classList.add('filled');
                }
            });
            
            updateCodeField();
            checkAllFilled();
            if (digits.length > 0) {
                codeInputs[Math.min(digits.length, codeInputs.length - 1)].focus();
            }
        });
    });

    // Focus inicial
    codeInputs[0].focus();
    
    // Iniciar countdown timer
    startCountdown();
}

function checkAllFilled() {
    const btnVerificar = document.getElementById('btnVerificar');
    if (btnVerificar) {
        const allFilled = codeInputs.every(input => input.value.length === 1);
        btnVerificar.disabled = !allFilled;
    }
}

function updateCodeField() {
    const code = codeInputs.map(input => input.value).join('');
    document.getElementById('code').value = code;
}

function clearCodeInputs() {
    codeInputs.forEach(input => {
        input.value = '';
        input.classList.remove('filled');
        input.classList.add('error');
    });
    setTimeout(() => {
        codeInputs.forEach(input => input.classList.remove('error'));
        codeInputs[0].focus();
        checkAllFilled();
    }, 500);
}

function startCountdown() {
    const timerDisplay = document.getElementById('timer');
    const resendLink = document.getElementById('resendLink');
    const countdownSpan = document.getElementById('countdown');
    
    if (!timerDisplay || !resendLink || !countdownSpan) return;
    
    // Mostrar timer, ocultar link
    timerDisplay.style.display = 'inline';
    resendLink.style.display = 'none';
    
    let timeLeft = 60;
    countdownSpan.textContent = timeLeft;
    
    // Limpiar intervalo anterior si existe
    if (countdownInterval) {
        clearInterval(countdownInterval);
    }
    
    countdownInterval = setInterval(() => {
        timeLeft--;
        countdownSpan.textContent = timeLeft;
        
        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
            timerDisplay.style.display = 'none';
            resendLink.style.display = 'inline';
        }
    }, 1000);
}

// Manejar el envío del formulario de verificación
document.getElementById('verificationForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const code = document.getElementById('code').value;
    const submitBtn = document.getElementById('btnVerificar');
    
    if (!code || code.length !== 6) {
        showAlert('Por favor ingresa un código válido de 6 dígitos', 'danger');
        return;
    }
    
    // Mostrar modal de loading
    const loadingModal = document.getElementById('loadingModal');
    loadingModal.classList.add('show');
    
    // Deshabilitar botón
    submitBtn.disabled = true;
    const originalHTML = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Verificando...';
    
    try {
        const formData = new URLSearchParams();
        formData.append('code', code);
        
        const response = await fetch('/auth/verify-code', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        });
        
        const result = await response.text();
        const data = JSON.parse(result);
        
        // Ocultar modal
        loadingModal.classList.remove('show');
        
        if (data.success) {
            showAlert('¡Código verificado correctamente!', 'success');
            // Redirigir después de 1 segundo
            setTimeout(() => {
                window.location.href = data.redirectUrl || '/registro';
            }, 1000);
        } else {
            showAlert(data.error || 'Código inválido. Intenta nuevamente.', 'danger');
            clearCodeInputs();
        }
        
    } catch (error) {
        // Ocultar modal
        loadingModal.classList.remove('show');
        console.error('Error:', error);
        showAlert('Error de conexión. Intenta nuevamente.', 'danger');
        clearCodeInputs();
    } finally {
        // Rehabilitar botón
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalHTML;
    }
});

// Manejar reenvío de código
document.addEventListener('DOMContentLoaded', function() {
    const resendLink = document.getElementById('resendLink');
    if (resendLink) {
        resendLink.addEventListener('click', async (e) => {
            e.preventDefault();
            
            // Obtener el email del formulario de email
            const email = document.getElementById('email').value;
            if (!email) {
                showAlert('No se pudo obtener el correo electrónico', 'danger');
                return;
            }
            
            try {
                const tipo = document.getElementById('tipoRegistro').value || 'usuario';
                const formData = new URLSearchParams();
                formData.append('email', email);
                formData.append('tipo', tipo);
                
                const response = await fetch('/auth/send-code', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: formData
                });
                
                const result = await response.text();
                const data = JSON.parse(result);
                
                if (data.success) {
                    showAlert('Código reenviado correctamente. Revisa tu correo.', 'success');
                    clearCodeInputs();
                    startCountdown();
                } else {
                    showAlert(data.error || 'Error al reenviar el código.', 'danger');
                }
            } catch (error) {
                console.error('Error:', error);
                showAlert('Error al reenviar el código. Intenta nuevamente.', 'danger');
            }
        });
    }
});

// Función para mostrar alertas dinámicas como modales
function showAlert(message, type = 'danger') {
    // Remover modales anteriores si existen
    const existingModal = document.getElementById('dynamicAlertModal');
    if (existingModal) {
        existingModal.remove();
    }
    
    const isSuccess = type === 'success';
    const iconClass = isSuccess ? 'fa-check-circle' : 'fa-exclamation-circle';
    const iconColor = isSuccess ? '#28a745' : '#dc3545';
    const titleColor = isSuccess ? '#28a745' : '#dc3545';
    const title = isSuccess ? '¡Éxito!' : 'Error';
    
    // Crear modal dinámicamente
    const modalHTML = `
        <div class="modal show" id="dynamicAlertModal">
            <div class="modal-content" style="max-width: 400px;">
                <i class="fas ${iconClass}" style="font-size: 60px; color: ${iconColor}; margin-bottom: 20px;"></i>
                <h3 style="color: ${titleColor}; margin-bottom: 15px; font-size: 24px;">${title}</h3>
                <p style="color: #666; margin-bottom: 25px;">${message}</p>
                <button type="button" class="btn btn-primary" onclick="this.closest('.modal').remove()" style="width: auto; padding: 10px 30px;">
                    Aceptar
                </button>
            </div>
        </div>
    `;
    
    // Insertar modal en el body
    document.body.insertAdjacentHTML('beforeend', modalHTML);
}

// Función para validar email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Inicialización
document.addEventListener('DOMContentLoaded', function() {
    showEmailForm();
});