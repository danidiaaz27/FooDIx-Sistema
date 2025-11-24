/**
 * registro-restaurante.js
 * Gestión del formulario de registro de restaurantes con validación de RUC
 */

// Constantes
const API_VALIDATION_URL = '/api/validation';
const RUC_LENGTH = 11;
const PHONE_LENGTH = 9;

// Estado de la aplicación
const appState = {
    isValidatingRUC: false,
    rucData: null,
    rucValidated: false
};

/**
 * Inicialización al cargar el DOM
 */
document.addEventListener('DOMContentLoaded', function() {
    initializeRestaurantForm();
});

/**
 * Inicializa los event listeners del formulario de restaurante
 */
function initializeRestaurantForm() {
    const rucInput = document.getElementById('RUC');
    const phoneInput = document.getElementById('TelefonoRestaurante');
    
    if (rucInput) {
        // Validación de RUC en tiempo real al completar 11 dígitos
        rucInput.addEventListener('input', handleRUCInput);
        
        // Prevenir entrada no numérica
        rucInput.addEventListener('keypress', onlyNumbers);
    }
    
    if (phoneInput) {
        // Validación de teléfono en tiempo real
        phoneInput.addEventListener('input', handlePhoneInput);
        
        // Prevenir entrada no numérica
        phoneInput.addEventListener('keypress', onlyNumbers);
    }
    
    // Prevenir submit si el RUC no está validado
    const restaurantForm = document.getElementById('restaurantForm');
    if (restaurantForm) {
        restaurantForm.addEventListener('submit', handleFormSubmit);
    }
}

/**
 * Maneja la entrada del campo RUC
 * @param {Event} event - Evento de input
 */
function handleRUCInput(event) {
    const input = event.target;
    const ruc = input.value.trim();
    
    // Limpiar caracteres no numéricos
    input.value = ruc.replace(/\D/g, '');
    
    // Resetear estado si el RUC cambia
    if (appState.rucValidated && input.value !== appState.rucData?.ruc) {
        resetRUCValidation();
    }
    
    // Validar automáticamente cuando se completen 11 dígitos
    if (input.value.length === RUC_LENGTH) {
        validateRUC(input.value);
    } else if (input.value.length > RUC_LENGTH) {
        // Limitar a 11 dígitos
        input.value = input.value.substring(0, RUC_LENGTH);
    } else {
        // Limpiar campos si hay menos de 11 dígitos
        clearRUCFields();
        removeValidationFeedback(input);
    }
}

/**
 * Valida el RUC consultando la API
 * @param {string} ruc - Número de RUC a validar
 */
async function validateRUC(ruc) {
    // Prevenir validaciones duplicadas
    if (appState.isValidatingRUC) {
        return;
    }
    
    const rucInput = document.getElementById('RUC');
    const spinner = showLoadingSpinner(rucInput);
    
    appState.isValidatingRUC = true;
    
    try {
        const response = await fetch(`${API_VALIDATION_URL}/ruc/${ruc}`);
        const data = await response.json();
        
        if (response.ok && data) {
            // RUC válido - poblar campos
            populateRUCFields(data);
            showValidationSuccess(rucInput, 'RUC verificado correctamente');
            
            appState.rucData = data;
            appState.rucValidated = true;
        } else {
            // RUC no encontrado o error
            showValidationError(rucInput, data.error || 'RUC no encontrado en SUNAT');
            clearRUCFields();
            appState.rucValidated = false;
        }
    } catch (error) {
        console.error('Error al validar RUC:', error);
        showValidationError(rucInput, 'Error al conectar con el servicio de validación');
        clearRUCFields();
        appState.rucValidated = false;
    } finally {
        hideLoadingSpinner(spinner);
        appState.isValidatingRUC = false;
    }
}

/**
 * Puebla los campos del formulario con los datos del RUC
 * @param {Object} data - Datos del RUC desde la API
 */
function populateRUCFields(data) {
    // Razón Social
    const razonSocialInput = document.getElementById('RazonSocial');
    if (razonSocialInput) {
        razonSocialInput.value = data.razonSocial || '';
        razonSocialInput.readOnly = true;
        razonSocialInput.classList.add('bg-light');
    }
    
    // Domicilio Fiscal
    const domicilioFiscalInput = document.getElementById('DomicilioFiscal');
    if (domicilioFiscalInput) {
        domicilioFiscalInput.value = data.direccion || '';
        domicilioFiscalInput.readOnly = true;
        domicilioFiscalInput.classList.add('bg-light');
    }
    
    console.log('✅ Campos de RUC poblados:', data);
}

/**
 * Limpia los campos relacionados con el RUC
 */
function clearRUCFields() {
    const fields = ['RazonSocial', 'DomicilioFiscal'];
    
    fields.forEach(fieldId => {
        const input = document.getElementById(fieldId);
        if (input) {
            input.value = '';
            input.readOnly = false;
            input.classList.remove('bg-light');
        }
    });
    
    appState.rucData = null;
    appState.rucValidated = false;
}

/**
 * Resetea la validación del RUC
 */
function resetRUCValidation() {
    clearRUCFields();
    const rucInput = document.getElementById('RUC');
    if (rucInput) {
        removeValidationFeedback(rucInput);
    }
}

/**
 * Maneja la validación del teléfono en tiempo real
 * @param {Event} event - Evento de input
 */
function handlePhoneInput(event) {
    const input = event.target;
    const phone = input.value.trim();
    
    // Limpiar caracteres no numéricos
    input.value = phone.replace(/\D/g, '');
    
    // Limitar a 9 dígitos
    if (input.value.length > PHONE_LENGTH) {
        input.value = input.value.substring(0, PHONE_LENGTH);
    }
    
    // Validar longitud
    if (input.value.length === PHONE_LENGTH) {
        showValidationSuccess(input, 'Teléfono válido');
    } else if (input.value.length > 0) {
        showValidationError(input, `Debe contener ${PHONE_LENGTH} dígitos`);
    } else {
        removeValidationFeedback(input);
    }
}

/**
 * Previene la entrada de caracteres no numéricos
 * @param {Event} event - Evento de keypress
 */
function onlyNumbers(event) {
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode < 48 || charCode > 57) {
        event.preventDefault();
        return false;
    }
    return true;
}

/**
 * Maneja el submit del formulario
 * @param {Event} event - Evento de submit
 */
function handleFormSubmit(event) {
    const rucInput = document.getElementById('RUC');
    const phoneInput = document.getElementById('TelefonoRestaurante');
    
    // Validar que el RUC esté verificado
    if (!appState.rucValidated) {
        event.preventDefault();
        showValidationError(rucInput, 'Debe ingresar un RUC válido');
        rucInput.focus();
        return false;
    }
    
    // Validar teléfono
    if (phoneInput && phoneInput.value.length !== PHONE_LENGTH) {
        event.preventDefault();
        showValidationError(phoneInput, `El teléfono debe tener ${PHONE_LENGTH} dígitos`);
        phoneInput.focus();
        return false;
    }
    
    // Los archivos ahora son opcionales - no se validan como requeridos
    // El modal se mostrará automáticamente después del registro exitoso
    
    return true;
}

/**
 * Muestra un spinner de carga junto al input
 * @param {HTMLElement} input - Elemento input
 * @returns {HTMLElement} - Elemento spinner creado
 */
function showLoadingSpinner(input) {
    const spinner = document.createElement('div');
    spinner.className = 'spinner-border spinner-border-sm text-primary ms-2';
    spinner.setAttribute('role', 'status');
    spinner.innerHTML = '<span class="visually-hidden">Validando...</span>';
    spinner.id = 'ruc-spinner';
    
    // Insertar después del input
    input.parentElement.appendChild(spinner);
    
    return spinner;
}

/**
 * Oculta y elimina el spinner de carga
 * @param {HTMLElement} spinner - Elemento spinner
 */
function hideLoadingSpinner(spinner) {
    if (spinner && spinner.parentElement) {
        spinner.parentElement.removeChild(spinner);
    }
}

/**
 * Muestra mensaje de validación exitosa
 * @param {HTMLElement} input - Elemento input
 * @param {string} message - Mensaje a mostrar
 */
function showValidationSuccess(input, message) {
    removeValidationFeedback(input);
    
    input.classList.remove('is-invalid');
    input.classList.add('is-valid');
    
    const feedback = document.createElement('div');
    feedback.className = 'valid-feedback d-block';
    feedback.textContent = message;
    input.parentElement.appendChild(feedback);
}

/**
 * Muestra mensaje de error de validación
 * @param {HTMLElement} input - Elemento input
 * @param {string} message - Mensaje de error
 */
function showValidationError(input, message) {
    removeValidationFeedback(input);
    
    input.classList.remove('is-valid');
    input.classList.add('is-invalid');
    
    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback d-block';
    feedback.textContent = message;
    input.parentElement.appendChild(feedback);
}

/**
 * Elimina mensajes de validación del input
 * @param {HTMLElement} input - Elemento input
 */
function removeValidationFeedback(input) {
    input.classList.remove('is-valid', 'is-invalid');
    
    const feedbacks = input.parentElement.querySelectorAll('.valid-feedback, .invalid-feedback');
    feedbacks.forEach(feedback => feedback.remove());
}
