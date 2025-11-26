/**
 * registroUsuario.js
 * Gestión del formulario de registro de usuarios (clientes)
 * Sistema simplificado sin multirol
 */

document.addEventListener('DOMContentLoaded', function() {
    // =============================================
    // REFERENCIAS A ELEMENTOS DEL DOM
    // =============================================
    const registerForm = document.getElementById('registerForm');
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
    const dniSpinner = document.getElementById('dniSpinner');
    const btnVerificarDNI = document.getElementById('btnVerificarDNI');
    
    const today = new Date();
    
    // =============================================
    // VALIDACIÓN DE EDAD MÍNIMA (18 AÑOS)
    // =============================================
    const maxDate = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());
    if (birthDateInput) {
        birthDateInput.max = maxDate.toISOString().split('T')[0];
    }
    
    // =============================================
    // MOSTRAR/OCULTAR CONTRASEÑA
    // =============================================
    if (togglePasswordBtn) {
        togglePasswordBtn.addEventListener('click', function() {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            this.querySelector('i').classList.toggle('fa-eye');
            this.querySelector('i').classList.toggle('fa-eye-slash');
        });
    }
    
    // =============================================
    // AUTO-COMPLETADO DE DNI
    // =============================================
    async function consultarDNI(dni) {
        try {
            if (dniSpinner) dniSpinner.style.display = 'inline-block';
            if (btnVerificarDNI) btnVerificarDNI.disabled = true;
            if (docNumberHelp) {
                docNumberHelp.textContent = '🔍 Consultando DNI...';
                docNumberHelp.className = 'form-text text-primary';
            }
            
            const response = await fetch(`/api/validation/dni/${dni}`);
            
            if (!response.ok) {
                throw new Error('DNI no encontrado');
            }
            
            const data = await response.json();
            
            // Auto-completar los campos
            if (nameInput) {
                nameInput.value = data.nombres || '';
                nameInput.readOnly = true;
                nameInput.style.backgroundColor = '#e7f5e7';
            }
            if (paternalInput) {
                paternalInput.value = data.apellidoPaterno || '';
                paternalInput.readOnly = true;
                paternalInput.style.backgroundColor = '#e7f5e7';
            }
            if (maternalInput) {
                maternalInput.value = data.apellidoMaterno || '';
                maternalInput.readOnly = true;
                maternalInput.style.backgroundColor = '#e7f5e7';
            }
            
            // Mensaje de éxito
            if (docNumberHelp) {
                docNumberHelp.textContent = '✓ DNI verificado correctamente';
                docNumberHelp.className = 'form-text text-success';
            }
            if (docNumberInput) {
                docNumberInput.classList.remove('is-invalid');
                docNumberInput.classList.add('is-valid');
            }
            
        } catch (error) {
            console.error('Error consultando DNI:', error);
            
            // Limpiar campos y permitir edición manual
            if (nameInput) {
                nameInput.value = '';
                nameInput.readOnly = false;
                nameInput.style.backgroundColor = '';
            }
            if (paternalInput) {
                paternalInput.value = '';
                paternalInput.readOnly = false;
                paternalInput.style.backgroundColor = '';
            }
            if (maternalInput) {
                maternalInput.value = '';
                maternalInput.readOnly = false;
                maternalInput.style.backgroundColor = '';
            }
            
            // Mensaje de error
            if (docNumberHelp) {
                docNumberHelp.textContent = '✗ DNI no encontrado. Ingrese los datos manualmente';
                docNumberHelp.className = 'form-text text-danger';
            }
            if (docNumberInput) {
                docNumberInput.classList.remove('is-valid');
                docNumberInput.classList.add('is-invalid');
            }
            
        } finally {
            if (dniSpinner) dniSpinner.style.display = 'none';
            if (btnVerificarDNI) btnVerificarDNI.disabled = false;
        }
    }
    
    // =============================================
    // VALIDACIÓN TIPO DE DOCUMENTO
    // =============================================
    if (docTypeSelect) {
        docTypeSelect.addEventListener('change', function() {
            const selectedType = this.value;
            
            if (selectedType === '1') { // DNI
                if (docNumberInput) {
                    docNumberInput.setAttribute('maxlength', '8');
                    docNumberInput.setAttribute('pattern', '[0-9]{8}');
                    docNumberInput.setAttribute('title', 'Debe contener exactamente 8 dígitos');
                }
                if (docNumberHelp) {
                    docNumberHelp.textContent = '8 dígitos numéricos - Se autocompletará al escribir';
                }
                if (btnVerificarDNI) {
                    btnVerificarDNI.style.display = 'block';
                }
            } else if (selectedType === '2' || selectedType === '3') { // CE o Pasaporte
                if (docNumberInput) {
                    docNumberInput.setAttribute('maxlength', '15');
                    docNumberInput.setAttribute('pattern', '[A-Za-z0-9]{1,15}');
                    docNumberInput.setAttribute('title', 'Máximo 15 caracteres alfanuméricos');
                }
                if (docNumberHelp) {
                    docNumberHelp.textContent = 'Máximo 15 caracteres alfanuméricos';
                }
                if (btnVerificarDNI) {
                    btnVerificarDNI.style.display = 'none';
                }
            } else {
                if (docNumberInput) {
                    docNumberInput.removeAttribute('maxlength');
                    docNumberInput.removeAttribute('pattern');
                    docNumberInput.removeAttribute('title');
                }
                if (docNumberHelp) {
                    docNumberHelp.textContent = 'Ingrese el número de documento';
                }
                if (btnVerificarDNI) {
                    btnVerificarDNI.style.display = 'none';
                }
            }
            
            if (docNumberInput) docNumberInput.value = '';
        });
    }
    
    // Auto-consultar cuando se complete el DNI (8 dígitos)
    if (docNumberInput) {
        docNumberInput.addEventListener('input', function() {
            if (docTypeSelect && docTypeSelect.value === '1') { // Solo para DNI
                this.value = this.value.replace(/[^0-9]/g, '');
                
                // Limpiar clases de validación
                this.classList.remove('is-valid', 'is-invalid');
                
                if (this.value.length === 8) {
                    // Auto-consultar cuando tenga 8 dígitos
                    consultarDNI(this.value);
                } else if (this.value.length > 0 && docNumberHelp) {
                    docNumberHelp.textContent = `Ingresa ${8 - this.value.length} dígitos más`;
                    docNumberHelp.className = 'form-text text-muted';
                }
            }
        });
    }
    
    // Botón manual de verificación DNI
    if (btnVerificarDNI) {
        btnVerificarDNI.addEventListener('click', function() {
            const dni = docNumberInput ? docNumberInput.value : '';
            if (dni.length === 8) {
                consultarDNI(dni);
            } else {
                alert('Por favor ingrese un DNI válido de 8 dígitos');
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN DE CONTRASEÑA SEGURA
    // =============================================
    function validatePassword(password) {
        const minLength = password.length >= 8;
        const hasUppercase = /[A-Z]/.test(password);
        const hasLowercase = /[a-z]/.test(password);
        const hasNumber = /[0-9]/.test(password);
        const hasSymbol = /[!@#$%^&*(),.?":{}|<>]/.test(password);

        // Actualizar visualización de requisitos
        const reqLength = document.getElementById('reqLength');
        const reqUppercase = document.getElementById('reqUppercase');
        const reqLowercase = document.getElementById('reqLowercase');
        const reqNumber = document.getElementById('reqNumber');
        const reqSymbol = document.getElementById('reqSymbol');
        
        if (reqLength) {
            reqLength.className = minLength ? 'text-success small' : 'text-danger small';
            reqLength.textContent = (minLength ? '✓' : '✗') + ' 8+ caracteres';
        }
        if (reqUppercase) {
            reqUppercase.className = hasUppercase ? 'text-success small' : 'text-danger small';
            reqUppercase.textContent = (hasUppercase ? '✓' : '✗') + ' 1 mayúscula';
        }
        if (reqLowercase) {
            reqLowercase.className = hasLowercase ? 'text-success small' : 'text-danger small';
            reqLowercase.textContent = (hasLowercase ? '✓' : '✗') + ' 1 minúscula';
        }
        if (reqNumber) {
            reqNumber.className = hasNumber ? 'text-success small' : 'text-danger small';
            reqNumber.textContent = (hasNumber ? '✓' : '✗') + ' 1 número';
        }
        if (reqSymbol) {
            reqSymbol.className = hasSymbol ? 'text-success small' : 'text-danger small';
            reqSymbol.textContent = (hasSymbol ? '✓' : '✗') + ' 1 símbolo';
        }

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

    // Validación en tiempo real de la contraseña
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            validatePassword(this.value);
            validatePasswordMatch();
        });
    }

    // =============================================
    // VALIDACIÓN DE COINCIDENCIA DE CONTRASEÑAS
    // =============================================
    function validatePasswordMatch() {
        if (!passwordInput || !confirmPasswordInput) return;
        
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        
        if (confirmPassword === '') {
            confirmPasswordInput.classList.remove('password-mismatch', 'is-valid');
            if (passwordMatchText) {
                passwordMatchText.textContent = 'Las contraseñas deben coincidir';
                passwordMatchText.className = 'form-text text-muted';
            }
            return;
        }
        
        if (password === confirmPassword && password !== '') {
            confirmPasswordInput.classList.remove('password-mismatch', 'is-invalid');
            confirmPasswordInput.classList.add('is-valid');
            if (passwordMatchText) {
                passwordMatchText.textContent = '✓ Las contraseñas coinciden';
                passwordMatchText.className = 'form-text text-success';
            }
        } else {
            confirmPasswordInput.classList.remove('is-valid');
            confirmPasswordInput.classList.add('password-mismatch', 'is-invalid');
            if (passwordMatchText) {
                passwordMatchText.textContent = '✗ Las contraseñas no coinciden';
                passwordMatchText.className = 'form-text text-danger';
            }
        }
    }
    
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', validatePasswordMatch);
    }
    
    // =============================================
    // VALIDACIÓN DE SOLO LETRAS (NOMBRES)
    // =============================================
    function validateLettersOnly(input) {
        if (!input) return;
        input.addEventListener('input', function() {
            this.value = this.value.replace(/[^A-Za-záéíóúÁÉÍÓÚñÑ\s]/g, '');
        });
    }
    
    validateLettersOnly(nameInput);
    validateLettersOnly(paternalInput);
    validateLettersOnly(maternalInput);
    
    // =============================================
    // VALIDACIÓN DE TELÉFONO (9 DÍGITOS)
    // =============================================
    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
            
            if (this.value.length > 9) {
                this.value = this.value.slice(0, 9);
            }
            
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
    }
    
    // =============================================
    // CASCADA DEPARTAMENTO → PROVINCIA → DISTRITO
    // =============================================
    if (deptSelect) {
        deptSelect.addEventListener('change', async function() {
            const deptId = this.value;
            if (provSelect) {
                provSelect.disabled = true;
                provSelect.innerHTML = '<option value="">Cargando...</option>';
            }
            if (distSelect) {
                distSelect.disabled = true;
                distSelect.innerHTML = '<option value="">Seleccione provincia primero</option>';
            }
            
            if (deptId) {
                try {
                    const response = await fetch(`/api/provincias/${deptId}`);
                    const provincias = await response.json();
                    
                    if (provSelect) {
                        provSelect.innerHTML = '<option value="">Seleccionar</option>';
                        provincias.forEach(prov => {
                            provSelect.innerHTML += `<option value="${prov.codigo}">${prov.nombre}</option>`;
                        });
                        provSelect.disabled = false;
                    }
                } catch (error) {
                    console.error('Error cargando provincias:', error);
                    if (provSelect) {
                        provSelect.innerHTML = '<option value="">Error al cargar</option>';
                    }
                }
            } else {
                if (provSelect) {
                    provSelect.innerHTML = '<option value="">Seleccione departamento primero</option>';
                }
            }
        });
    }
    
    if (provSelect) {
        provSelect.addEventListener('change', async function() {
            const provId = this.value;
            if (distSelect) {
                distSelect.disabled = true;
                distSelect.innerHTML = '<option value="">Cargando...</option>';
            }
            
            if (provId) {
                try {
                    const response = await fetch(`/api/distritos/${provId}`);
                    const distritos = await response.json();
                    
                    if (distSelect) {
                        distSelect.innerHTML = '<option value="">Seleccionar</option>';
                        distritos.forEach(dist => {
                            distSelect.innerHTML += `<option value="${dist.codigo}">${dist.nombre}</option>`;
                        });
                        distSelect.disabled = false;
                    }
                } catch (error) {
                    console.error('Error cargando distritos:', error);
                    if (distSelect) {
                        distSelect.innerHTML = '<option value="">Error al cargar</option>';
                    }
                }
            } else {
                if (distSelect) {
                    distSelect.innerHTML = '<option value="">Seleccione provincia primero</option>';
                }
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN FINAL DEL FORMULARIO
    // =============================================
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            let isValid = true;
            const errors = [];
            
            // Validar contraseña segura
            if (passwordInput && !validatePassword(passwordInput.value)) {
                errors.push('La contraseña debe cumplir con todos los requisitos de seguridad');
                isValid = false;
            }
            
            // Validar coincidencia de contraseñas
            if (passwordInput && confirmPasswordInput && passwordInput.value !== confirmPasswordInput.value) {
                confirmPasswordInput.classList.add('password-mismatch');
                errors.push('Las contraseñas no coinciden');
                isValid = false;
            }
            
            // Validar edad mínima
            if (birthDateInput) {
                const birthDate = new Date(birthDateInput.value);
                const age = Math.floor((today - birthDate) / (365.25 * 24 * 60 * 60 * 1000));
                if (age < 18) {
                    birthDateInput.classList.add('invalid-input');
                    errors.push('Debes ser mayor de 18 años');
                    isValid = false;
                }
            }
            
            // Validar documento según tipo
            if (docTypeSelect && docNumberInput) {
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
            }
            
            // Validar teléfono si se ingresó
            if (phoneInput && phoneInput.value && !/^[0-9]{9}$/.test(phoneInput.value)) {
                phoneInput.classList.add('invalid-input');
                errors.push('El teléfono debe tener exactamente 9 dígitos');
                isValid = false;
            }
            
            if (!isValid) {
                event.preventDefault();
                alert('Errores en el formulario:\n- ' + errors.join('\n- '));
            }
        });
    }

    // =============================================
    // AUTOCOMPLETAR EMAIL DESDE URL (VERIFICACIÓN)
    // =============================================
    const urlParams = new URLSearchParams(window.location.search);
    const verifiedEmail = urlParams.get('email');
    if (verifiedEmail) {
        const emailInput = document.getElementById('CorreoElectronico');
        if (emailInput) {
            emailInput.value = verifiedEmail;
            emailInput.readOnly = true;
            emailInput.style.backgroundColor = '#e9ecef';
        }
    }
});
