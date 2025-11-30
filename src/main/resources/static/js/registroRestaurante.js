// =============================================
// REGISTRORESTAURANTE.JS - Sistema de Registro para Restaurantes
// Maneja validación de datos personales (Paso 1) y datos del restaurante (Paso 2)
// =============================================

document.addEventListener('DOMContentLoaded', function() {
    // =============================================
    // DETECCIÓN DE PASO INICIAL (viene del servidor después de PASO 1)
    // =============================================
    const pasoInicial = window.mostrarPasoInicial; // 'restaurante' o null
    
    // =============================================
    // REFERENCIAS A ELEMENTOS DEL DOM
    // =============================================
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
    const toggleConfirmPasswordBtn = document.getElementById('toggleConfirmarContrasena');
    const dniSpinner = document.getElementById('dniSpinner');
    const btnVerificarDNI = document.getElementById('btnVerificarDNI');
    const passwordRequirementsPopup = document.getElementById('passwordRequirements');
    
    // =============================================
    // REFERENCIAS A FORMULARIOS
    // =============================================
    const registerForm = document.getElementById('registerForm');
    const restaurantForm = document.getElementById('restaurantForm');
    const btnSiguiente = document.getElementById('btnSiguiente');
    const rolInput = document.querySelector('input[name="codigoRol"]');
    
    // Inicializar formularios
    if (registerForm) registerForm.style.display = 'block';
    if (restaurantForm) restaurantForm.style.display = 'none';
    
    // =============================================
    // MOSTRAR MODAL INFORMATIVO AL CARGAR LA PÁGINA
    // =============================================
    const modalInfoRestaurante = new bootstrap.Modal(document.getElementById('modalInfoRestaurante'));
    modalInfoRestaurante.show();
    
    // =============================================
    // FUNCIÓN PARA MOSTRAR NOTIFICACIONES COMO MODALES
    // =============================================
    function showNotificationModal(message, type = 'info') {
        const modal = document.getElementById('notificationModal');
        const modalHeader = document.getElementById('modalHeader');
        const modalTitle = document.getElementById('modalTitleText');
        const modalIcon = document.getElementById('modalIcon');
        const modalMessage = document.getElementById('modalMessage');
        const modalCloseBtn = document.getElementById('modalCloseBtn');
        
        // Limpiar clases previas
        modalHeader.className = 'modal-header border-0';
        modalCloseBtn.className = 'btn btn-sm';
        modalIcon.className = 'me-2';
        
        // Configurar según el tipo
        switch(type) {
            case 'success':
                modalHeader.classList.add('success');
                modalIcon.classList.add('fas', 'fa-check-circle');
                modalTitle.textContent = '¡Éxito!';
                modalCloseBtn.classList.add('btn-success');
                break;
            case 'error':
                modalHeader.classList.add('error');
                modalIcon.classList.add('fas', 'fa-exclamation-circle');
                modalTitle.textContent = 'Error';
                modalCloseBtn.classList.add('btn-danger');
                break;
            case 'warning':
                modalHeader.classList.add('warning');
                modalIcon.classList.add('fas', 'fa-exclamation-triangle');
                modalTitle.textContent = 'Advertencia';
                modalCloseBtn.classList.add('btn-warning');
                break;
            case 'info':
            default:
                modalHeader.classList.add('info');
                modalIcon.classList.add('fas', 'fa-info-circle');
                modalTitle.textContent = 'Información';
                modalCloseBtn.classList.add('btn-info');
                break;
        }
        
        modalMessage.textContent = message;
        
        // Mostrar modal
        const notificationModal = new bootstrap.Modal(modal);
        notificationModal.show();
        
        // Auto-cerrar después de 5 segundos (solo para success e info)
        if (type === 'success' || type === 'info') {
            setTimeout(() => {
                notificationModal.hide();
            }, 5000);
        }
    }
    
    // =============================================
    // VERIFICAR SI HAY MENSAJES DEL SERVIDOR AL CARGAR
    // =============================================
    window.addEventListener('load', function() {
        const serverMessage = document.getElementById('serverMessage');
        const serverError = document.getElementById('serverError');
        
        if (serverMessage && serverMessage.dataset.message) {
            showNotificationModal(serverMessage.dataset.message, 'success');
        }
        
        if (serverError && serverError.dataset.message) {
            showNotificationModal(serverError.dataset.message, 'error');
        }
    });
    
    // =============================================
    // SI VIENE DEL SERVIDOR (PASO 2), MOSTRAR EL FORMULARIO CORRESPONDIENTE
    // =============================================
    if (pasoInicial && pasoInicial === 'restaurante') {
        console.log('🏪 [PASO 2] Mostrando formulario de restaurante');
        
        // Ocultar formulario de datos personales
        if (registerForm) registerForm.style.display = 'none';
        
        // Mostrar formulario de restaurante
        if (restaurantForm) {
            restaurantForm.style.display = 'block';
            restaurantForm.classList.add('active');
        }
        
        if (rolInput) rolInput.value = '2';
    }
    
    // =============================================
    // BOTÓN SIGUIENTE - CAMBIA AL PASO 2 (SIN ENVIAR AL SERVIDOR)
    // =============================================
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Validar formulario
            if (!registerForm.checkValidity()) {
                registerForm.reportValidity();
                return;
            }
            
            // Validar que las contraseñas coincidan
            if (!validatePasswordMatch()) {
                showNotificationModal('Las contraseñas no coinciden. Por favor, verifica que ambas sean idénticas.', 'warning');
                return;
            }
            
            // Validar contraseña fuerte
            if (!validatePassword()) {
                showNotificationModal('La contraseña no cumple con los requisitos mínimos de seguridad. Debe tener al menos 8 caracteres, mayúsculas, minúsculas, números y símbolos.', 'warning');
                return;
            }
            
            console.log('📝 [PASO 1] Validación exitosa, enviando al servidor...');
            console.log('📝 [PASO 1] Rol seleccionado: Restaurante (2)');
            
            // Enviar formulario al servidor (POST /registro)
            // El servidor creará el usuario y redirigirá a /registro-restaurante para el paso 2
            registerForm.submit();
        });
    }
    
    // =============================================
    // BOTÓN VOLVER (del formulario de restaurante al personal)
    // =============================================
    const btnVolverRestaurante = document.getElementById('btnVolverRestaurante');
    if (btnVolverRestaurante) {
        btnVolverRestaurante.addEventListener('click', function() {
            restaurantForm.style.display = 'none';
            restaurantForm.classList.remove('active');
            registerForm.style.display = 'block';
            
            // Scroll hacia arriba
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }
    
    // =============================================
    // VALIDACIÓN DE EDAD MÍNIMA (18 AÑOS)
    // =============================================
    const today = new Date();
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
    
    if (toggleConfirmPasswordBtn) {
        toggleConfirmPasswordBtn.addEventListener('click', function() {
            const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            confirmPasswordInput.setAttribute('type', type);
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
    
    // Evento para tipo de documento
    if (docTypeSelect) {
        docTypeSelect.addEventListener('change', function() {
            const tipo = this.value;
            if (tipo === '1') { // DNI
                docNumberInput.maxLength = 8;
                docNumberInput.pattern = '[0-9]{8}';
                docNumberInput.placeholder = '8 dígitos';
                if (docNumberHelp) docNumberHelp.textContent = '8 dígitos numéricos - Se autocompletará al escribir';
                if (btnVerificarDNI) btnVerificarDNI.style.display = 'none';
            } else if (tipo === '2') { // Carnet de Extranjería
                docNumberInput.maxLength = 12;
                docNumberInput.pattern = '[0-9]{12}';
                docNumberInput.placeholder = '12 dígitos';
                if (docNumberHelp) docNumberHelp.textContent = '12 dígitos numéricos';
                if (btnVerificarDNI) btnVerificarDNI.style.display = 'none';
                resetNameFields();
            } else if (tipo === '3') { // Pasaporte
                docNumberInput.maxLength = 12;
                docNumberInput.pattern = '[A-Z0-9]{7,12}';
                docNumberInput.placeholder = 'Ej: AB123456';
                if (docNumberHelp) docNumberHelp.textContent = '7-12 caracteres alfanuméricos';
                if (btnVerificarDNI) btnVerificarDNI.style.display = 'none';
                resetNameFields();
            }
            
            // Limpiar validaciones previas
            docNumberInput.value = '';
            docNumberInput.classList.remove('is-valid', 'is-invalid');
            resetNameFields();
        });
    }
    
    // Evento para número de documento (auto-consulta DNI)
    if (docNumberInput) {
        docNumberInput.addEventListener('input', function() {
            const tipo = docTypeSelect.value;
            const numero = this.value;
            
            // Si es DNI y tiene 8 dígitos, auto-consultar
            if (tipo === '1' && numero.length === 8 && /^\d{8}$/.test(numero)) {
                consultarDNI(numero);
            } else if (tipo === '1') {
                // Si DNI pero menos de 8 dígitos, resetear
                if (docNumberHelp) {
                    docNumberHelp.textContent = '8 dígitos numéricos - Se autocompletará al escribir';
                    docNumberHelp.className = 'form-text text-muted';
                }
                docNumberInput.classList.remove('is-valid', 'is-invalid');
            }
        });
    }
    
    function resetNameFields() {
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
    }
    
    // =============================================
    // VALIDACIÓN DE CONTRASEÑA
    // =============================================
    function validatePassword() {
        const password = passwordInput.value;
        const requirements = {
            length: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            lowercase: /[a-z]/.test(password),
            number: /[0-9]/.test(password),
            symbol: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)
        };
        
        // Actualizar indicadores visuales en el popup
        const reqLength = document.getElementById('reqLength');
        const reqUppercase = document.getElementById('reqUppercase');
        const reqLowercase = document.getElementById('reqLowercase');
        const reqNumber = document.getElementById('reqNumber');
        const reqSymbol = document.getElementById('reqSymbol');
        
        if (reqLength) {
            if (requirements.length) {
                reqLength.className = 'requirement-item valid';
                reqLength.querySelector('i').className = 'fas fa-circle-check';
            } else {
                reqLength.className = 'requirement-item invalid';
                reqLength.querySelector('i').className = 'fas fa-circle-xmark';
            }
        }
        
        if (reqUppercase) {
            if (requirements.uppercase) {
                reqUppercase.className = 'requirement-item valid';
                reqUppercase.querySelector('i').className = 'fas fa-circle-check';
            } else {
                reqUppercase.className = 'requirement-item invalid';
                reqUppercase.querySelector('i').className = 'fas fa-circle-xmark';
            }
        }
        
        if (reqLowercase) {
            if (requirements.lowercase) {
                reqLowercase.className = 'requirement-item valid';
                reqLowercase.querySelector('i').className = 'fas fa-circle-check';
            } else {
                reqLowercase.className = 'requirement-item invalid';
                reqLowercase.querySelector('i').className = 'fas fa-circle-xmark';
            }
        }
        
        if (reqNumber) {
            if (requirements.number) {
                reqNumber.className = 'requirement-item valid';
                reqNumber.querySelector('i').className = 'fas fa-circle-check';
            } else {
                reqNumber.className = 'requirement-item invalid';
                reqNumber.querySelector('i').className = 'fas fa-circle-xmark';
            }
        }
        
        if (reqSymbol) {
            if (requirements.symbol) {
                reqSymbol.className = 'requirement-item valid';
                reqSymbol.querySelector('i').className = 'fas fa-circle-check';
            } else {
                reqSymbol.className = 'requirement-item invalid';
                reqSymbol.querySelector('i').className = 'fas fa-circle-xmark';
            }
        }
        
        // Validar el input visualmente
        if (passwordInput) {
            if (password.length === 0) {
                passwordInput.classList.remove('is-valid', 'is-invalid');
            } else if (Object.values(requirements).every(v => v)) {
                passwordInput.classList.remove('is-invalid');
                passwordInput.classList.add('is-valid');
            } else {
                passwordInput.classList.remove('is-valid');
                passwordInput.classList.add('is-invalid');
            }
        }
        
        return Object.values(requirements).every(v => v);
    }
    
    // Mostrar popup cuando el usuario enfoca el campo de contraseña
    if (passwordInput) {
        passwordInput.addEventListener('focus', function() {
            if (passwordRequirementsPopup) {
                passwordRequirementsPopup.classList.add('show');
            }
        });
        
        passwordInput.addEventListener('blur', function() {
            // Ocultar después de un pequeño delay para mejor UX
            setTimeout(() => {
                if (passwordRequirementsPopup) {
                    passwordRequirementsPopup.classList.remove('show');
                }
            }, 200);
        });
        
        passwordInput.addEventListener('input', function() {
            validatePassword();
            validatePasswordMatch();
        });
    }
    
    // =============================================
    // VALIDACIÓN DE COINCIDENCIA DE CONTRASEÑAS
    // =============================================
    function validatePasswordMatch() {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        
        if (confirmPassword === '') {
            confirmPasswordInput.classList.remove('password-mismatch', 'is-valid');
            if (passwordMatchText) {
                passwordMatchText.textContent = 'Las contraseñas deben coincidir';
                passwordMatchText.className = 'form-text text-muted';
            }
            return false;
        }
        
        if (password !== confirmPassword) {
            confirmPasswordInput.classList.add('password-mismatch');
            confirmPasswordInput.classList.remove('is-valid');
            if (passwordMatchText) {
                passwordMatchText.textContent = '✗ Las contraseñas no coinciden';
                passwordMatchText.className = 'form-text text-danger';
            }
            return false;
        } else {
            confirmPasswordInput.classList.remove('password-mismatch');
            confirmPasswordInput.classList.add('is-valid');
            if (passwordMatchText) {
                passwordMatchText.textContent = '✓ Las contraseñas coinciden';
                passwordMatchText.className = 'form-text text-success';
            }
            return true;
        }
    }
    
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', validatePasswordMatch);
    }
    
    // =============================================
    // VALIDACIÓN DE NOMBRES (SOLO LETRAS)
    // =============================================
    function validateLettersOnly(input) {
        const value = input.value;
        const regex = /^[A-Za-záéíóúÁÉÍÓÚñÑ\s]+$/;
        
        if (value && !regex.test(value)) {
            input.classList.add('invalid-input');
            return false;
        } else {
            input.classList.remove('invalid-input');
            return true;
        }
    }
    
    if (nameInput) {
        nameInput.addEventListener('input', function() {
            validateLettersOnly(this);
        });
    }
    if (paternalInput) {
        paternalInput.addEventListener('input', function() {
            validateLettersOnly(this);
        });
    }
    if (maternalInput) {
        maternalInput.addEventListener('input', function() {
            validateLettersOnly(this);
        });
    }
    
    // =============================================
    // CASCADA DE SELECTS: DEPARTAMENTO → PROVINCIA → DISTRITO (Personal)
    // =============================================
    if (deptSelect) {
        deptSelect.addEventListener('change', async function() {
            const codigoDepartamento = this.value;
            
            // Resetear provincia y distrito
            provSelect.innerHTML = '<option value="">Seleccionar provincia</option>';
            provSelect.disabled = true;
            distSelect.innerHTML = '<option value="">Seleccione provincia primero</option>';
            distSelect.disabled = true;
            
            if (!codigoDepartamento) return;
            
            try {
                console.log('🌎 Cargando provincias para departamento:', codigoDepartamento);
                const response = await fetch(`/api/provincias/${codigoDepartamento}`);
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const provincias = await response.json();
                console.log('✅ Provincias cargadas:', provincias.length);
                
                provSelect.innerHTML = '<option value="">Seleccionar provincia</option>';
                provincias.forEach(prov => {
                    provSelect.innerHTML += `<option value="${prov.codigo}">${prov.nombre}</option>`;
                });
                provSelect.disabled = false;
            } catch (error) {
                console.error('❌ Error cargando provincias:', error);
                provSelect.innerHTML = '<option value="">Error al cargar provincias</option>';
            }
        });
    }
    
    if (provSelect) {
        provSelect.addEventListener('change', async function() {
            const codigoProvincia = this.value;
            
            // Resetear distrito
            distSelect.innerHTML = '<option value="">Seleccionar distrito</option>';
            distSelect.disabled = true;
            
            if (!codigoProvincia) return;
            
            try {
                console.log('🏘️ Cargando distritos para provincia:', codigoProvincia);
                const response = await fetch(`/api/distritos/${codigoProvincia}`);
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const distritos = await response.json();
                console.log('✅ Distritos cargados:', distritos.length);
                
                distSelect.innerHTML = '<option value="">Seleccionar distrito</option>';
                distritos.forEach(dist => {
                    distSelect.innerHTML += `<option value="${dist.codigo}">${dist.nombre}</option>`;
                });
                distSelect.disabled = false;
            } catch (error) {
                console.error('❌ Error cargando distritos:', error);
                distSelect.innerHTML = '<option value="">Error al cargar distritos</option>';
            }
        });
    }
    
    // =============================================
    // CASCADA DE SELECTS: DEPARTAMENTO → PROVINCIA → DISTRITO (Negocio)
    // =============================================
    const deptNegocioSelect = document.getElementById('DepartamentoNegocio');
    const provNegocioSelect = document.getElementById('ProvinciaNegocio');
    const distNegocioSelect = document.getElementById('DistritoNegocio');
    
    if (deptNegocioSelect) {
        deptNegocioSelect.addEventListener('change', async function() {
            const codigoDepartamento = this.value;
            
            // Resetear provincia y distrito
            provNegocioSelect.innerHTML = '<option value="">Seleccionar provincia</option>';
            provNegocioSelect.disabled = true;
            distNegocioSelect.innerHTML = '<option value="">Seleccione provincia primero</option>';
            distNegocioSelect.disabled = true;
            
            if (!codigoDepartamento) return;
            
            try {
                console.log('🌎 [NEGOCIO] Cargando provincias para departamento:', codigoDepartamento);
                const response = await fetch(`/api/provincias/${codigoDepartamento}`);
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const provincias = await response.json();
                console.log('✅ [NEGOCIO] Provincias cargadas:', provincias.length);
                
                provNegocioSelect.innerHTML = '<option value="">Seleccionar provincia</option>';
                provincias.forEach(prov => {
                    provNegocioSelect.innerHTML += `<option value="${prov.codigo}">${prov.nombre}</option>`;
                });
                provNegocioSelect.disabled = false;
            } catch (error) {
                console.error('❌ Error cargando provincias del negocio:', error);
                provNegocioSelect.innerHTML = '<option value="">Error al cargar provincias</option>';
            }
        });
    }
    
    if (provNegocioSelect) {
        provNegocioSelect.addEventListener('change', async function() {
            const codigoProvincia = this.value;
            
            // Resetear distrito
            distNegocioSelect.innerHTML = '<option value="">Seleccionar distrito</option>';
            distNegocioSelect.disabled = true;
            
            if (!codigoProvincia) return;
            
            try {
                console.log('🏘️ [NEGOCIO] Cargando distritos para provincia:', codigoProvincia);
                const response = await fetch(`/api/distritos/${codigoProvincia}`);
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const distritos = await response.json();
                console.log('✅ [NEGOCIO] Distritos cargados:', distritos.length);
                
                distNegocioSelect.innerHTML = '<option value="">Seleccionar distrito</option>';
                distritos.forEach(dist => {
                    distNegocioSelect.innerHTML += `<option value="${dist.codigo}">${dist.nombre}</option>`;
                });
                distNegocioSelect.disabled = false;
            } catch (error) {
                console.error('❌ Error cargando distritos del negocio:', error);
                distNegocioSelect.innerHTML = '<option value="">Error al cargar distritos</option>';
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN FINAL ANTES DE ENVIAR
    // =============================================
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            // Validar contraseñas
            if (!validatePassword()) {
                e.preventDefault();
                showNotificationModal('La contraseña no cumple con los requisitos mínimos de seguridad.', 'warning');
                return false;
            }
            
            if (!validatePasswordMatch()) {
                e.preventDefault();
                showNotificationModal('Las contraseñas no coinciden.', 'warning');
                return false;
            }
            
            // Validar campos de nombres
            if (!validateLettersOnly(nameInput) || 
                !validateLettersOnly(paternalInput) || 
                !validateLettersOnly(maternalInput)) {
                e.preventDefault();
                showNotificationModal('Los nombres solo deben contener letras y espacios.', 'warning');
                return false;
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN DE FORMULARIO DE RESTAURANTE
    // =============================================
    if (restaurantForm) {
        restaurantForm.addEventListener('submit', function(e) {
            if (!this.checkValidity()) {
                e.preventDefault();
                this.reportValidity();
                return false;
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN DE RUC (PASO 2 - RESTAURANTE)
    // =============================================
    const rucInput = document.getElementById('RUC');
    const razonSocialInput = document.getElementById('RazonSocial');
    const domicilioFiscalInput = document.getElementById('DomicilioFiscal');
    
    let rucValidado = false;
    
    if (rucInput) {
        rucInput.addEventListener('input', async function() {
            const ruc = this.value.trim();
            
            // Limpiar validación si el RUC cambia
            if (ruc.length < 11) {
                rucValidado = false;
                if (razonSocialInput) razonSocialInput.value = '';
                if (domicilioFiscalInput) domicilioFiscalInput.value = '';
                return;
            }
            
            // Validar cuando llegue a 11 dígitos
            if (ruc.length === 11 && /^\d{11}$/.test(ruc)) {
                try {
                    console.log('🔍 Validando RUC:', ruc);
                    const response = await fetch(`/api/validation/ruc/${ruc}`);
                    
                    if (!response.ok) {
                        throw new Error('RUC no encontrado');
                    }
                    
                    const data = await response.json();
                    
                    // Auto-completar campos
                    if (razonSocialInput) {
                        razonSocialInput.value = data.razonSocial || '';
                        razonSocialInput.readOnly = true;
                        razonSocialInput.style.backgroundColor = '#e7f5e7';
                    }
                    if (domicilioFiscalInput) {
                        domicilioFiscalInput.value = data.direccion || '';
                        domicilioFiscalInput.readOnly = true;
                        domicilioFiscalInput.style.backgroundColor = '#e7f5e7';
                    }
                    
                    // Marcar como válido
                    rucInput.classList.remove('is-invalid');
                    rucInput.classList.add('is-valid');
                    rucValidado = true;
                    console.log('✅ RUC validado:', data);
                    
                } catch (error) {
                    console.error('❌ Error validando RUC:', error);
                    rucInput.classList.remove('is-valid');
                    rucInput.classList.add('is-invalid');
                    rucValidado = false;
                    
                    // Limpiar campos y permitir edición manual
                    if (razonSocialInput) {
                        razonSocialInput.value = '';
                        razonSocialInput.readOnly = false;
                        razonSocialInput.style.backgroundColor = '';
                    }
                    if (domicilioFiscalInput) {
                        domicilioFiscalInput.value = '';
                        domicilioFiscalInput.readOnly = false;
                        domicilioFiscalInput.style.backgroundColor = '';
                    }
                }
            }
        });
    }
});
