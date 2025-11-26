// =============================================
// REGISTRONEGOCIO.JS - Sistema de Registro Multirol para Negocios
// Solo Restaurante y Repartidor (sin Cliente)
// =============================================

document.addEventListener('DOMContentLoaded', function() {
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
    const dniSpinner = document.getElementById('dniSpinner');
    const btnVerificarDNI = document.getElementById('btnVerificarDNI');
    
    // =============================================
    // SISTEMA MULTIROL (SOLO RESTAURANTE Y REPARTIDOR)
    // =============================================
    let currentUserType = 'restaurante'; // Por defecto restaurante
    const typeButtons = document.querySelectorAll('.type-btn');
    const registerForm = document.getElementById('registerForm');
    const restaurantForm = document.getElementById('restaurantForm');
    const deliveryForm = document.getElementById('deliveryForm');
    const btnSiguiente = document.getElementById('btnSiguiente');
    const rolInput = document.querySelector('input[name="CodigoRol"]');
    
    // Inicializar formularios
    if (registerForm) registerForm.style.display = 'block';
    if (restaurantForm) restaurantForm.style.display = 'none';
    if (deliveryForm) deliveryForm.style.display = 'none';
    
    // Auto-seleccionar tipo de usuario si viene desde index.html
    if (businessType) {
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
    
    // Botón Siguiente
    if (btnSiguiente) {
        btnSiguiente.addEventListener('click', function() {
            if (!registerForm.checkValidity()) {
                registerForm.reportValidity();
                return;
            }
            
            registerForm.style.display = 'none';
            if (currentUserType === 'restaurante') {
                restaurantForm.style.display = 'block';
                restaurantForm.classList.add('active');
            } else if (currentUserType === 'repartidor') {
                deliveryForm.style.display = 'block';
                deliveryForm.classList.add('active');
            }
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
            const select = document.getElementById('TipoVehiculo');
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
        
        // Actualizar indicadores visuales
        const reqLength = document.getElementById('reqLength');
        const reqUppercase = document.getElementById('reqUppercase');
        const reqLowercase = document.getElementById('reqLowercase');
        const reqNumber = document.getElementById('reqNumber');
        const reqSymbol = document.getElementById('reqSymbol');
        
        if (reqLength) {
            reqLength.className = requirements.length ? 'text-success small' : 'text-danger small';
            reqLength.innerHTML = (requirements.length ? '✓' : '✗') + ' 8+ caracteres';
        }
        
        if (reqUppercase) {
            reqUppercase.className = requirements.uppercase ? 'text-success small' : 'text-danger small';
            reqUppercase.innerHTML = (requirements.uppercase ? '✓' : '✗') + ' 1 mayúscula';
        }
        
        if (reqLowercase) {
            reqLowercase.className = requirements.lowercase ? 'text-success small' : 'text-danger small';
            reqLowercase.innerHTML = (requirements.lowercase ? '✓' : '✗') + ' 1 minúscula';
        }
        
        if (reqNumber) {
            reqNumber.className = requirements.number ? 'text-success small' : 'text-danger small';
            reqNumber.innerHTML = (requirements.number ? '✓' : '✗') + ' 1 número';
        }
        
        if (reqSymbol) {
            reqSymbol.className = requirements.symbol ? 'text-success small' : 'text-danger small';
            reqSymbol.innerHTML = (requirements.symbol ? '✓' : '✗') + ' 1 símbolo';
        }
        
        return Object.values(requirements).every(v => v);
    }
    
    if (passwordInput) {
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
    // CASCADA DE SELECTS: DEPARTAMENTO → PROVINCIA → DISTRITO
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
                const response = await fetch(`/api/provincias?departamento=${codigoDepartamento}`);
                
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
                const response = await fetch(`/api/distritos?provincia=${codigoProvincia}`);
                
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
