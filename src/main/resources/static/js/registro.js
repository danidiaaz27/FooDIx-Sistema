document.addEventListener('DOMContentLoaded', function() {
    const docTypeSelect = document.getElementById('CodigoTipoDocumento');
    const docNumberInput = document.getElementById('NumeroDocumento');
    const docNumberHelp = document.getElementById('docNumberHelp');
    const passwordInput = document.getElementById('Contrasena');
    const confirmPasswordInput = document.getElementById('ConfirmarContrasena');
    const passwordMatchText = document.getElementById('passwordMatchText');
    const nameInput = document.getElementById('Nombre');
    const paternalInput = document.getElementById('ApellidoPaterno');
    const maternalInput = document.getElementById('ApellidoMaterno');
    const phoneInput = document.getElementById('Telefono');
    const birthDateInput = document.getElementById('FechaNacimiento');
    const deptSelect = document.getElementById('CodigoDepartamento');
    const provSelect = document.getElementById('CodigoProvincia');
    const distSelect = document.getElementById('CodigoDistrito');
    const togglePasswordBtn = document.getElementById('toggleContrasena');
    
    // Validación de edad mínima (18 años)
    const today = new Date();
    const maxDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
    birthDateInput.max = maxDate.toISOString().split('T')[0];
    
    // Función para mostrar/ocultar contraseña
    togglePasswordBtn.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        this.querySelector('i').classList.toggle('fa-eye');
        this.querySelector('i').classList.toggle('fa-eye-slash');
    });
    
    // Validación de tipo de documento y número
    docTypeSelect.addEventListener('change', function() {
        const selectedType = this.value;
        
        if (selectedType === '1') { // DNI
            docNumberInput.setAttribute('maxlength', '8');
            docNumberInput.setAttribute('pattern', '[0-9]{8}');
            docNumberInput.setAttribute('title', 'Debe contener exactamente 8 dígitos');
            docNumberHelp.textContent = '8 dígitos numéricos';
        } else if (selectedType === '2' || selectedType === '3') { // CE o Pasaporte
            docNumberInput.setAttribute('maxlength', '15');
            docNumberInput.setAttribute('pattern', '[A-Za-z0-9]{1,15}');
            docNumberInput.setAttribute('title', 'Máximo 15 caracteres alfanuméricos');
            docNumberHelp.textContent = 'Máximo 15 caracteres alfanuméricos';
        } else {
            docNumberInput.removeAttribute('maxlength');
            docNumberInput.removeAttribute('pattern');
            docNumberInput.removeAttribute('title');
            docNumberHelp.textContent = 'Ingrese el número de documento';
        }
        
        docNumberInput.value = '';
    });
    
    // Validación de contraseña segura
    function validatePassword(password) {
        const minLength = password.length >= 8;
        const hasUppercase = /[A-Z]/.test(password);
        const hasLowercase = /[a-z]/.test(password);
        const hasNumber = /[0-9]/.test(password);
        const hasSymbol = /[!@#$%^&*(),.?":{}|<>]/.test(password);

        // Actualizar visualización de requisitos
        document.getElementById('reqLength').className = minLength ? 'text-success' : 'text-danger';
        document.getElementById('reqLength').textContent = (minLength ? '✓' : '✗') + ' Mínimo 8 caracteres';

        document.getElementById('reqUppercase').className = hasUppercase ? 'text-success' : 'text-danger';
        document.getElementById('reqUppercase').textContent = (hasUppercase ? '✓' : '✗') + ' Una mayúscula';

        document.getElementById('reqLowercase').className = hasLowercase ? 'text-success' : 'text-danger';
        document.getElementById('reqLowercase').textContent = (hasLowercase ? '✓' : '✗') + ' Una minúscula';

        document.getElementById('reqNumber').className = hasNumber ? 'text-success' : 'text-danger';
        document.getElementById('reqNumber').textContent = (hasNumber ? '✓' : '✗') + ' Al menos un número';

        document.getElementById('reqSymbol').className = hasSymbol ? 'text-success' : 'text-danger';
        document.getElementById('reqSymbol').textContent = (hasSymbol ? '✓' : '✗') + ' Al menos un símbolo';

        // Validar el input visualmente
        if (password.length === 0) {
            passwordInput.classList.remove('is-valid', 'is-invalid');
        } else if (minLength && hasUppercase && hasLowercase && hasNumber && hasSymbol) {
            passwordInput.classList.remove('is-invalid');
            passwordInput.classList.add('is-valid');
        } else {
            passwordInput.classList.remove('is-valid');
            passwordInput.classList.add('is-invalid');
        }

        return minLength && hasUppercase && hasLowercase && hasNumber && hasSymbol;
    }

    // Validación en tiempo real de la contraseña
    passwordInput.addEventListener('input', function() {
        validatePassword(this.value);
        validatePasswordMatch(); // También validar coincidencia
    });

    // Validación de coincidencia de contraseñas
    function validatePasswordMatch() {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        
        if (confirmPassword === '') {
            confirmPasswordInput.classList.remove('password-mismatch', 'is-valid');
            passwordMatchText.textContent = 'Las contraseñas deben coincidir';
            passwordMatchText.className = 'form-text text-muted';
            return;
        }
        
        if (password === confirmPassword && password !== '') {
            confirmPasswordInput.classList.remove('password-mismatch', 'is-invalid');
            confirmPasswordInput.classList.add('is-valid');
            passwordMatchText.textContent = '✓ Las contraseñas coinciden';
            passwordMatchText.className = 'form-text text-success';
        } else {
            confirmPasswordInput.classList.remove('is-valid');
            confirmPasswordInput.classList.add('password-mismatch', 'is-invalid');
            passwordMatchText.textContent = '✗ Las contraseñas no coinciden';
            passwordMatchText.className = 'form-text text-danger';
        }
    }
    
    confirmPasswordInput.addEventListener('input', validatePasswordMatch);
    
    // Validación de solo letras para nombres y apellidos
    function validateLettersOnly(input) {
        input.addEventListener('input', function() {
            this.value = this.value.replace(/[^A-Za-záéíóúÁÉÍÓÚñÑ\s]/g, '');
        });
    }
    
    validateLettersOnly(nameInput);
    validateLettersOnly(paternalInput);
    validateLettersOnly(maternalInput);
    
    // Validación de solo números para teléfono (máximo 9 dígitos)
    phoneInput.addEventListener('input', function() {
        // Eliminar cualquier caracter que no sea número
        this.value = this.value.replace(/[^0-9]/g, '');
        
        // Limitar a 9 dígitos
        if (this.value.length > 9) {
            this.value = this.value.slice(0, 9);
        }
        
        // Validación visual en tiempo real
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
    
    // Validación de solo números para DNI
    docNumberInput.addEventListener('input', function() {
        if (docTypeSelect.value === '1') {
            this.value = this.value.replace(/[^0-9]/g, '');
        }
    });
    
    // Cascada Departamento -> Provincia -> Distrito
    deptSelect.addEventListener('change', async function() {
        const deptId = this.value;
        provSelect.disabled = true;
        distSelect.disabled = true;
        provSelect.innerHTML = '<option value="">Cargando...</option>';
        distSelect.innerHTML = '<option value="">Seleccione provincia primero</option>';
        
        if (deptId) {
            try {
                // Llamada AJAX para cargar provincias
                const response = await fetch(`/api/provincias/${deptId}`);
                const provincias = await response.json();
                
                provSelect.innerHTML = '<option value="">Seleccionar</option>';
                provincias.forEach(prov => {
                    provSelect.innerHTML += `<option value="${prov.codigo}">${prov.nombre}</option>`;
                });
                provSelect.disabled = false;
            } catch (error) {
                console.error('Error cargando provincias:', error);
                provSelect.innerHTML = '<option value="">Error al cargar</option>';
            }
        } else {
            provSelect.innerHTML = '<option value="">Seleccione departamento primero</option>';
        }
    });
    
    provSelect.addEventListener('change', async function() {
        const provId = this.value;
        distSelect.disabled = true;
        distSelect.innerHTML = '<option value="">Cargando...</option>';
        
        if (provId) {
            try {
                // Llamada AJAX para cargar distritos
                const response = await fetch(`/api/distritos/${provId}`);
                const distritos = await response.json();
                
                distSelect.innerHTML = '<option value="">Seleccionar</option>';
                distritos.forEach(dist => {
                    distSelect.innerHTML += `<option value="${dist.codigo}">${dist.nombre}</option>`;
                });
                distSelect.disabled = false;
            } catch (error) {
                console.error('Error cargando distritos:', error);
                distSelect.innerHTML = '<option value="">Error al cargar</option>';
            }
        } else {
            distSelect.innerHTML = '<option value="">Seleccione provincia primero</option>';
        }
    });
    
    // Validación del formulario antes de enviar
    document.getElementById('registerForm').addEventListener('submit', function(event) {
        let isValid = true;
        const errors = [];
        
        // Validar contraseña segura
        if (!validatePassword(passwordInput.value)) {
            errors.push('La contraseña debe cumplir con todos los requisitos de seguridad');
            isValid = false;
        }
        
        // Validar coincidencia de contraseñas
        if (passwordInput.value !== confirmPasswordInput.value) {
            confirmPasswordInput.classList.add('password-mismatch');
            errors.push('Las contraseñas no coinciden');
            isValid = false;
        }
        
        // Validar edad mínima
        const birthDate = new Date(birthDateInput.value);
        const age = Math.floor((today - birthDate) / (365.25 * 24 * 60 * 60 * 1000));
        if (age < 18) {
            birthDateInput.classList.add('invalid-input');
            errors.push('Debes ser mayor de 18 años');
            isValid = false;
        }
        
        // Validar documento según tipo
        const docType = docTypeSelect.value;
        const docNumber = docNumberInput.value;
        
        if (docType === '1' && !/^[0-9]{8}$/.test(docNumber)) {
            docNumberInput.classList.add('invalid-input');
            errors.push('El DNI debe tener exactamente 8 dígitos');
            isValid = false;
        }
        
        if ((docType === '2' || docType === '3') && !/^[A-Za-z0-9]{1,15}$/.test(docNumber)) {
            docNumberInput.classList.add('invalid-input');
            errors.push('Número de documento inválido');
            isValid = false;
        }
        
        // Validar teléfono si se ingresó
        if (phoneInput.value && !/^[0-9]{9}$/.test(phoneInput.value)) {
            phoneInput.classList.add('invalid-input');
            errors.push('El teléfono debe tener exactamente 9 dígitos');
            isValid = false;
        }
        
        if (!isValid) {
            event.preventDefault();
            alert('Errores en el formulario:\n- ' + errors.join('\n- '));
        }
    });

    // Autocompletar email desde URL (para verificación)
    const urlParams = new URLSearchParams(window.location.search);
    const verifiedEmail = urlParams.get('email');
    if (verifiedEmail) {
        const emailInput = document.getElementById('CorreoElectronico');
        emailInput.value = verifiedEmail;
        emailInput.readOnly = true;
        emailInput.style.backgroundColor = '#e9ecef';
    }
});