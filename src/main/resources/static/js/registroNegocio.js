// =============================================
// REGISTRONEGOCIO.JS - Sistema de Registro Multirol para Negocios
// Solo Restaurante y Repartidor (sin Cliente)
// =============================================

document.addEventListener('DOMContentLoaded', function() {
    // =============================================
    // DETECCIÓN DE PASO INICIAL (viene del servidor después de PASO 1)
    // =============================================
    const pasoInicial = window.mostrarPasoInicial; // 'restaurante' o 'repartidor' o null
    
    // =============================================
    // DETECCIÓN DE SELECCIÓN PREVIA DESDE INDEX
    // =============================================
    // Detectar tipo desde parámetro URL o localStorage
    const urlParams = new URLSearchParams(window.location.search);
    const tipoFromUrl = urlParams.get('tipo');
    const businessType = tipoFromUrl || localStorage.getItem('selectedBusinessType');
    
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
    // SISTEMA MULTIROL (SOLO RESTAURANTE Y REPARTIDOR)
    // =============================================
    let currentUserType = 'restaurante'; // Por defecto restaurante
    const typeButtons = document.querySelectorAll('.type-btn');
    const registerForm = document.getElementById('registerForm');
    const restaurantForm = document.getElementById('restaurantForm');
    const deliveryForm = document.getElementById('deliveryForm');
    const btnSiguiente = document.getElementById('btnSiguiente');
    const rolInput = document.querySelector('input[name="codigoRol"]');
    
    // Inicializar formularios
    if (registerForm) registerForm.style.display = 'block';
    if (restaurantForm) restaurantForm.style.display = 'none';
    if (deliveryForm) deliveryForm.style.display = 'none';
    
    // SI VIENE DEL SERVIDOR (PASO 2), MOSTRAR EL FORMULARIO CORRESPONDIENTE
    if (pasoInicial) {
        console.log('🔄 [PASO 2] Detectado paso inicial desde servidor:', pasoInicial);
        
        // Ocultar formulario de datos personales
        if (registerForm) registerForm.style.display = 'none';
        
        // Mostrar formulario correspondiente
        if (pasoInicial === 'restaurante') {
            console.log('🏪 [PASO 2] Mostrando formulario de restaurante');
            if (restaurantForm) {
                restaurantForm.style.display = 'block';
                restaurantForm.classList.add('active');
            }
            // Seleccionar botón de restaurante
            const btnRestaurante = document.querySelector('[data-type="restaurante"]');
            if (btnRestaurante) {
                typeButtons.forEach(b => b.classList.remove('active'));
                btnRestaurante.classList.add('active');
            }
            currentUserType = 'restaurante';
            if (rolInput) rolInput.value = '2';
            
        } else if (pasoInicial === 'repartidor') {
            console.log('🚴 [PASO 2] Mostrando formulario de repartidor');
            if (deliveryForm) {
                deliveryForm.style.display = 'block';
                deliveryForm.classList.add('active');
            }
            // Seleccionar botón de repartidor
            const btnRepartidor = document.querySelector('[data-type="repartidor"]');
            if (btnRepartidor) {
                typeButtons.forEach(b => b.classList.remove('active'));
                btnRepartidor.classList.add('active');
            }
            currentUserType = 'repartidor';
            if (rolInput) rolInput.value = '3';
            cargarTiposVehiculo();
        }
    }
    // Auto-seleccionar tipo de usuario si viene desde index.html
    else if (businessType) {
        console.log('🎯 Tipo de negocio detectado desde localStorage:', businessType);
        
        // Buscar y hacer clic en el botón correspondiente
        const targetButton = document.querySelector(`[data-type="${businessType}"]`);
        if (targetButton) {
            targetButton.click();
            console.log('✅ Auto-selección aplicada:', businessType);
        }
        
        // Limpiar localStorage después de usar
        localStorage.removeItem('selectedBusinessType');
        console.log('🧹 localStorage limpiado');
    }
    
    // Manejar cambio de tipo de usuario
    typeButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            typeButtons.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            
            currentUserType = this.dataset.type;
            
            // Mostrar formulario personal
            if (registerForm) registerForm.style.display = 'block';
            if (restaurantForm) restaurantForm.style.display = 'none';
            if (deliveryForm) deliveryForm.style.display = 'none';
            
            // Configurar según tipo
            if (currentUserType === 'restaurante') {
                btnSiguiente.style.display = 'block';
                rolInput.value = '2';
                // Mostrar modal informativo
                const modal = new bootstrap.Modal(document.getElementById('modalInfoRestaurante'));
                modal.show();
            } else if (currentUserType === 'repartidor') {
                btnSiguiente.style.display = 'block';
                rolInput.value = '3';
                // Mostrar modal informativo
                const modal = new bootstrap.Modal(document.getElementById('modalInfoRepartidor'));
                modal.show();
                cargarTiposVehiculo();
            }
        });
    });
    
    // Botón Siguiente - ENVÍA EL FORMULARIO AL SERVIDOR (PASO 1)
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', function(e) {
            e.preventDefault(); // Prevenir comportamiento por defecto
            
            // Validar formulario
            if (!registerForm.checkValidity()) {
                registerForm.reportValidity();
                return;
            }
            
            // Validar que las contraseñas coincidan
            if (!validatePasswordMatch()) {
                alert('Las contraseñas no coinciden.');
                return;
            }
            
            // Validar contraseña fuerte
            if (!validatePassword()) {
                alert('La contraseña no cumple con los requisitos mínimos.');
                return;
            }
            
            console.log('📝 [PASO 1] Enviando datos personales al servidor...');
            console.log('📝 [PASO 1] Rol seleccionado:', rolInput.value);
            
            // Enviar el formulario al servidor (POST /registro)
            // El servidor creará el usuario y redirigirá a /registro-restaurante o /registro-repartidor
            registerForm.submit();
        });
    }
    
    // Botones Volver
    const btnVolverRestaurante = document.getElementById('btnVolverRestaurante');
    if (btnVolverRestaurante) {
        btnVolverRestaurante.addEventListener('click', function() {
            restaurantForm.style.display = 'none';
            restaurantForm.classList.remove('active');
            registerForm.style.display = 'block';
        });
    }
    
    const btnVolverRepartidor = document.getElementById('btnVolverRepartidor');
    if (btnVolverRepartidor) {
        btnVolverRepartidor.addEventListener('click', function() {
            deliveryForm.style.display = 'none';
            deliveryForm.classList.remove('active');
            registerForm.style.display = 'block';
        });
    }
    
    // Cargar tipos de vehículo
    async function cargarTiposVehiculo() {
        try {
            const response = await fetch('/api/tipos-vehiculo');
            const tipos = await response.json();
            const select = document.getElementById('codigoTipoVehiculo');
            if (select) {
                select.innerHTML = '<option value="">Seleccionar</option>';
                tipos.forEach(tipo => {
                    select.innerHTML += `<option value="${tipo.codigo}">${tipo.nombre}</option>`;
                });
            }
        } catch (error) {
            console.error('Error cargando tipos de vehículo:', error);
        }
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
                
                console.log('📬 Response status:', response.status);
                console.log('📬 Response headers:', response.headers.get('content-type'));
                
                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('❌ Error HTTP:', response.status, errorText);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const contentType = response.headers.get('content-type');
                const responseText = await response.text();
                console.log('📄 Response raw (first 200 chars):', responseText.substring(0, 200));
                
                if (!contentType || !contentType.includes('application/json')) {
                    console.error('❌ Content-Type incorrecto:', contentType);
                    console.error('❌ Respuesta completa:', responseText);
                    throw new Error('La respuesta no es JSON válido');
                }
                
                const provincias = JSON.parse(responseText);
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
                
                console.log('📬 Response status:', response.status);
                console.log('📬 Response headers:', response.headers.get('content-type'));
                
                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('❌ Error HTTP:', response.status, errorText);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const contentType = response.headers.get('content-type');
                const responseText = await response.text();
                console.log('📄 Response raw (first 200 chars):', responseText.substring(0, 200));
                
                if (!contentType || !contentType.includes('application/json')) {
                    console.error('❌ Content-Type incorrecto:', contentType);
                    console.error('❌ Respuesta completa:', responseText);
                    throw new Error('La respuesta no es JSON válido');
                }
                
                const distritos = JSON.parse(responseText);
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
                alert('La contraseña no cumple con los requisitos mínimos.');
                return false;
            }
            
            if (!validatePasswordMatch()) {
                e.preventDefault();
                alert('Las contraseñas no coinciden.');
                return false;
            }
            
            // Validar campos de nombres
            if (!validateLettersOnly(nameInput) || 
                !validateLettersOnly(paternalInput) || 
                !validateLettersOnly(maternalInput)) {
                e.preventDefault();
                alert('Los nombres solo deben contener letras.');
                return false;
            }
        });
    }
    
    // =============================================
    // VALIDACIÓN DE FORMULARIOS DE NEGOCIOS
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
    
    if (deliveryForm) {
        deliveryForm.addEventListener('submit', function(e) {
            if (!this.checkValidity()) {
                e.preventDefault();
                this.reportValidity();
                return false;
            }
        });
    }
});
