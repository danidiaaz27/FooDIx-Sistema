// =============================================
// ADMIN-FORM-VALIDATION.JS
// Validaciones para formulario de creación de administradores
// =============================================

document.addEventListener('DOMContentLoaded', function() {
    // =============================================
    // REFERENCIAS A ELEMENTOS DEL FORMULARIO
    // =============================================
    const adminForm = document.querySelector('#manageAdminsModal form');
    const adminNameInput = document.getElementById('adminName');
    const adminLastNameInput = document.getElementById('adminLastName');
    const adminEmailInput = document.getElementById('adminEmail');
    const adminPasswordInput = document.getElementById('adminPassword');
    const adminPasswordConfirmInput = document.getElementById('adminPasswordConfirm');
    const adminPhoneInput = document.getElementById('adminPhone');
    
    if (!adminForm) return; // Si no existe el formulario, salir
    
    // =============================================
    // VALIDACIÓN DE SOLO LETRAS (NOMBRE Y APELLIDO)
    // =============================================
    function validateLettersOnly(input) {
        if (!input) return;
        input.addEventListener('input', function() {
            // Permitir solo letras, espacios y caracteres acentuados
            this.value = this.value.replace(/[^A-Za-záéíóúÁÉÍÓÚñÑ\s]/g, '');
            
            // Validación visual
            if (this.value.trim().length === 0) {
                this.classList.remove('is-valid', 'is-invalid');
            } else if (this.value.trim().length >= 2) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            } else {
                this.classList.remove('is-valid');
                this.classList.add('is-invalid');
            }
        });
    }
    
    validateLettersOnly(adminNameInput);
    validateLettersOnly(adminLastNameInput);
    
    // =============================================
    // VALIDACIÓN DE EMAIL
    // =============================================
    if (adminEmailInput) {
        adminEmailInput.addEventListener('blur', function() {
            const email = this.value.trim();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            
            if (email === '') {
                this.classList.remove('is-valid', 'is-invalid');
            } else if (emailRegex.test(email)) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            } else {
                this.classList.remove('is-valid');
                this.classList.add('is-invalid');
            }
        });
        
        // Limpiar validación al escribir
        adminEmailInput.addEventListener('input', function() {
            if (this.value.trim() === '') {
                this.classList.remove('is-valid', 'is-invalid');
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN DE CONTRASEÑA SEGURA
    // =============================================
    function validatePassword(password, passwordInput) {
        const minLength = password.length >= 8;
        const hasUppercase = /[A-Z]/.test(password);
        const hasLowercase = /[a-z]/.test(password);
        const hasNumber = /[0-9]/.test(password);
        const hasSymbol = /[!@#$%^&*(),.?":{}|<>]/.test(password);
        
        // Validar el input visualmente
        if (passwordInput) {
            if (password.length === 0) {
                passwordInput.classList.remove('is-valid', 'is-invalid');
            } else if (minLength && hasUppercase && hasLowercase && hasNumber && hasSymbol) {
                passwordInput.classList.remove('is-invalid');
                passwordInput.classList.add('is-valid');
            } else {
                passwordInput.classList.remove('is-valid');
                passwordInput.classList.add('is-invalid');
            }
        }
        
        return minLength && hasUppercase && hasLowercase && hasNumber && hasSymbol;
    }
    
    // Crear mensaje de ayuda para contraseña si no existe
    if (adminPasswordInput && !document.getElementById('adminPasswordHelp')) {
        const helpDiv = document.createElement('div');
        helpDiv.id = 'adminPasswordHelp';
        helpDiv.className = 'form-text mt-2';
        helpDiv.innerHTML = `
            <small class="d-block text-muted mb-1"><strong>Requisitos de contraseña:</strong></small>
            <small class="d-block" id="adminReqLength">✗ Mínimo 8 caracteres</small>
            <small class="d-block" id="adminReqUppercase">✗ Una mayúscula</small>
            <small class="d-block" id="adminReqLowercase">✗ Una minúscula</small>
            <small class="d-block" id="adminReqNumber">✗ Al menos un número</small>
            <small class="d-block" id="adminReqSymbol">✗ Al menos un símbolo (!@#$%^&*)</small>
        `;
        adminPasswordInput.parentElement.parentElement.appendChild(helpDiv);
    }
    
    // Validación en tiempo real de la contraseña
    if (adminPasswordInput) {
        adminPasswordInput.addEventListener('input', function() {
            const password = this.value;
            const minLength = password.length >= 8;
            const hasUppercase = /[A-Z]/.test(password);
            const hasLowercase = /[a-z]/.test(password);
            const hasNumber = /[0-9]/.test(password);
            const hasSymbol = /[!@#$%^&*(),.?":{}|<>]/.test(password);
            
            // Actualizar visualización de requisitos
            const reqLength = document.getElementById('adminReqLength');
            const reqUppercase = document.getElementById('adminReqUppercase');
            const reqLowercase = document.getElementById('adminReqLowercase');
            const reqNumber = document.getElementById('adminReqNumber');
            const reqSymbol = document.getElementById('adminReqSymbol');
            
            if (reqLength) {
                reqLength.className = minLength ? 'd-block text-success' : 'd-block text-danger';
                reqLength.textContent = (minLength ? '✓' : '✗') + ' Mínimo 8 caracteres';
            }
            if (reqUppercase) {
                reqUppercase.className = hasUppercase ? 'd-block text-success' : 'd-block text-danger';
                reqUppercase.textContent = (hasUppercase ? '✓' : '✗') + ' Una mayúscula';
            }
            if (reqLowercase) {
                reqLowercase.className = hasLowercase ? 'd-block text-success' : 'd-block text-danger';
                reqLowercase.textContent = (hasLowercase ? '✓' : '✗') + ' Una minúscula';
            }
            if (reqNumber) {
                reqNumber.className = hasNumber ? 'd-block text-success' : 'd-block text-danger';
                reqNumber.textContent = (hasNumber ? '✓' : '✗') + ' Al menos un número';
            }
            if (reqSymbol) {
                reqSymbol.className = hasSymbol ? 'd-block text-success' : 'd-block text-danger';
                reqSymbol.textContent = (hasSymbol ? '✓' : '✗') + ' Al menos un símbolo';
            }
            
            validatePassword(password, this);
            validatePasswordMatch();
        });
    }
    
    // =============================================
    // VALIDACIÓN DE COINCIDENCIA DE CONTRASEÑAS
    // =============================================
    function validatePasswordMatch() {
        if (!adminPasswordInput || !adminPasswordConfirmInput) return;
        
        const password = adminPasswordInput.value;
        const confirmPassword = adminPasswordConfirmInput.value;
        
        // Crear mensaje de ayuda si no existe
        let matchText = document.getElementById('adminPasswordMatchText');
        if (!matchText) {
            matchText = document.createElement('div');
            matchText.id = 'adminPasswordMatchText';
            matchText.className = 'form-text';
            adminPasswordConfirmInput.parentElement.parentElement.appendChild(matchText);
        }
        
        if (confirmPassword === '') {
            adminPasswordConfirmInput.classList.remove('password-mismatch', 'is-valid', 'is-invalid');
            matchText.textContent = 'Las contraseñas deben coincidir';
            matchText.className = 'form-text text-muted';
            return;
        }
        
        if (password === confirmPassword && password !== '') {
            adminPasswordConfirmInput.classList.remove('password-mismatch', 'is-invalid');
            adminPasswordConfirmInput.classList.add('is-valid');
            matchText.textContent = '✓ Las contraseñas coinciden';
            matchText.className = 'form-text text-success';
        } else {
            adminPasswordConfirmInput.classList.remove('is-valid');
            adminPasswordConfirmInput.classList.add('password-mismatch', 'is-invalid');
            matchText.textContent = '✗ Las contraseñas no coinciden';
            matchText.className = 'form-text text-danger';
        }
    }
    
    if (adminPasswordConfirmInput) {
        adminPasswordConfirmInput.addEventListener('input', validatePasswordMatch);
    }
    
    // =============================================
    // VALIDACIÓN DE TELÉFONO (9 DÍGITOS)
    // =============================================
    if (adminPhoneInput) {
        adminPhoneInput.addEventListener('input', function() {
            // Solo permitir números
            this.value = this.value.replace(/[^0-9]/g, '');
            
            // Limitar a 9 dígitos
            if (this.value.length > 9) {
                this.value = this.value.slice(0, 9);
            }
            
            // Validación visual
            if (this.value.length === 0) {
                this.classList.remove('is-valid', 'is-invalid');
            } else if (this.value.length === 9) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            } else {
                this.classList.remove('is-valid');
                this.classList.add('is-invalid');
            }
        });
        
        // Crear mensaje de ayuda si no existe
        if (!adminPhoneInput.nextElementSibling || !adminPhoneInput.nextElementSibling.classList.contains('form-text')) {
            const helpText = document.createElement('small');
            helpText.className = 'form-text text-muted';
            helpText.textContent = 'Debe contener exactamente 9 dígitos';
            adminPhoneInput.parentElement.appendChild(helpText);
        }
    }
    
    // =============================================
    // VALIDACIÓN ANTES DE ENVIAR EL FORMULARIO
    // =============================================
    adminForm.addEventListener('submit', function(event) {
        let isValid = true;
        const errors = [];
        
        // Validar nombre
        if (adminNameInput && adminNameInput.value.trim().length < 2) {
            adminNameInput.classList.add('is-invalid');
            errors.push('El nombre debe tener al menos 2 caracteres');
            isValid = false;
        }
        
        // Validar apellido
        if (adminLastNameInput && adminLastNameInput.value.trim().length < 2) {
            adminLastNameInput.classList.add('is-invalid');
            errors.push('El apellido debe tener al menos 2 caracteres');
            isValid = false;
        }
        
        // Validar email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (adminEmailInput && !emailRegex.test(adminEmailInput.value.trim())) {
            adminEmailInput.classList.add('is-invalid');
            errors.push('El email no es válido');
            isValid = false;
        }
        
        // Validar contraseña segura
        if (adminPasswordInput && !validatePassword(adminPasswordInput.value, adminPasswordInput)) {
            adminPasswordInput.classList.add('is-invalid');
            errors.push('La contraseña debe cumplir con todos los requisitos de seguridad');
            isValid = false;
        }
        
        // Validar coincidencia de contraseñas
        if (adminPasswordInput && adminPasswordConfirmInput && 
            adminPasswordInput.value !== adminPasswordConfirmInput.value) {
            adminPasswordConfirmInput.classList.add('is-invalid');
            errors.push('Las contraseñas no coinciden');
            isValid = false;
        }
        
        // Validar teléfono (si se proporcionó)
        if (adminPhoneInput && adminPhoneInput.value.trim() !== '' && adminPhoneInput.value.length !== 9) {
            adminPhoneInput.classList.add('is-invalid');
            errors.push('El teléfono debe tener exactamente 9 dígitos');
            isValid = false;
        }
        
        if (!isValid) {
            event.preventDefault();
            
            // Mostrar alerta con los errores
            const errorMsg = errors.join('\n• ');
            alert('Por favor, corrija los siguientes errores:\n\n• ' + errorMsg);
        }
    });
    
    // =============================================
    // LIMPIAR FORMULARIO AL CERRAR MODAL
    // =============================================
    const manageAdminsModal = document.getElementById('manageAdminsModal');
    if (manageAdminsModal) {
        manageAdminsModal.addEventListener('hidden.bs.modal', function() {
            // Limpiar formulario
            if (adminForm) {
                adminForm.reset();
            }
            
            // Limpiar clases de validación
            const inputs = [adminNameInput, adminLastNameInput, adminEmailInput, 
                          adminPasswordInput, adminPasswordConfirmInput, adminPhoneInput];
            inputs.forEach(input => {
                if (input) {
                    input.classList.remove('is-valid', 'is-invalid', 'password-mismatch');
                }
            });
            
            // Limpiar mensajes de ayuda de contraseña
            const passwordHelp = document.getElementById('adminPasswordHelp');
            if (passwordHelp) {
                passwordHelp.remove();
            }
            
            const matchText = document.getElementById('adminPasswordMatchText');
            if (matchText) {
                matchText.remove();
            }
        });
    }
});

// =============================================
// FUNCIÓN GLOBAL PARA MOSTRAR/OCULTAR CONTRASEÑA
// =============================================
function togglePasswordVisibility(inputId, buttonElement) {
    const input = document.getElementById(inputId);
    if (!input) return;
    
    const icon = buttonElement.querySelector('i');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}
