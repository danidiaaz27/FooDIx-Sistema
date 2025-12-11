/**
 * JWT Helper - Utilidades para manejo de tokens JWT
 * FoodIx Sistema de Promociones
 */

/**
 * Verifica si existe un token JWT válido
 * @returns {boolean} true si existe token, false en caso contrario
 */
function hasJwtToken() {
    const token = localStorage.getItem('jwt_token');
    return token !== null && token !== '';
}

/**
 * Obtiene el token JWT del localStorage
 * @returns {string|null} Token JWT o null si no existe
 */
function getJwtToken() {
    return localStorage.getItem('jwt_token');
}

/**
 * Cierra la sesión eliminando el token JWT
 * Realiza petición POST al endpoint /logout y redirige
 */
function cerrarSesion() {
    console.log('🚪 [JWT] Cerrando sesión...');
    
    // Eliminar token de localStorage
    localStorage.removeItem('jwt_token');
    
    // Eliminar cookie
    document.cookie = 'jwt_token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=Lax';
    
    // Limpiar otros datos de sesión
    const keysToRemove = [];
    for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        if (key && (key.includes('user') || key.includes('auth') || key.includes('session'))) {
            keysToRemove.push(key);
        }
    }
    keysToRemove.forEach(key => localStorage.removeItem(key));
    
    console.log('✅ [JWT] Sesión cerrada en cliente, enviando petición al servidor...');
    
    // Obtener token CSRF desde meta tags o input hidden
    let csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    
    // Si no está en meta, buscar en input hidden
    if (!csrfToken) {
        csrfToken = document.querySelector('input[name="_csrf"]')?.value;
    }
    
    console.log('🔍 [JWT] Token CSRF encontrado:', csrfToken ? 'Sí' : 'No');
    
    // Crear un formulario oculto para hacer POST a /logout
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/logout';
    form.style.display = 'none';
    
    // Agregar token CSRF con el nombre correcto (_csrf)
    if (csrfToken) {
        const csrfInput = document.createElement('input');
        csrfInput.type = 'hidden';
        csrfInput.name = '_csrf';
        csrfInput.value = csrfToken;
        form.appendChild(csrfInput);
        console.log('✅ [JWT] Token CSRF agregado al formulario:', csrfToken.substring(0, 10) + '...');
    } else {
        console.error('❌ [JWT] No se encontró token CSRF - El logout puede fallar');
    }
    
    document.body.appendChild(form);
    form.submit();
}

/**
 * Verifica que el usuario tenga un token JWT válido
 * Si no tiene token, redirige al login
 * Usar al inicio de cada vista protegida
 */
function verificarAutenticacion() {
    const token = getJwtToken();
    
    if (!token) {
        console.warn('⚠️ [JWT] No se encontró token de autenticación');
        console.warn('⚠️ [JWT] Redirigiendo a login...');
        window.location.href = '/login?error=session_expired';
        return false;
    }
    
    console.log('✅ [JWT] Token encontrado, usuario autenticado');
    return true;
}

/**
 * Decodifica un token JWT (sin verificar firma)
 * ADVERTENCIA: No usar para validación de seguridad, solo para leer datos
 * @param {string} token Token JWT
 * @returns {object} Payload decodificado del token
 */
function decodeJwtToken(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('❌ [JWT] Error al decodificar token:', e);
        return null;
    }
}

/**
 * Obtiene el rol del usuario del token JWT
 * @returns {string|null} Rol del usuario (ej: "ROLE_ADMIN") o null
 */
function getUserRole() {
    const token = getJwtToken();
    if (!token) return null;
    
    const payload = decodeJwtToken(token);
    return payload ? payload.rol : null;
}

/**
 * Obtiene el username (email) del usuario del token JWT
 * @returns {string|null} Email del usuario o null
 */
function getUserEmail() {
    const token = getJwtToken();
    if (!token) return null;
    
    const payload = decodeJwtToken(token);
    return payload ? payload.sub : null;
}

/**
 * Verifica si el token JWT ha expirado
 * @returns {boolean} true si el token ha expirado, false en caso contrario
 */
function isTokenExpired() {
    const token = getJwtToken();
    if (!token) return true;
    
    const payload = decodeJwtToken(token);
    if (!payload || !payload.exp) return true;
    
    const now = Math.floor(Date.now() / 1000);
    return payload.exp < now;
}

/**
 * Configura los headers para peticiones fetch con token JWT
 * @returns {object} Headers configurados con Authorization
 */
function getAuthHeaders() {
    const token = getJwtToken();
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
    };
}

// Verificar autenticación al cargar la página (si no estamos en páginas públicas)
document.addEventListener('DOMContentLoaded', function() {
    const publicPages = ['/login', '/registro', '/registroUsuario', '/registroRestaurante', 
                        '/registroDelivery', '/verificacion', '/recuperar-password', 
                        '/verificar-codigo', '/cambiar-password', '/contacto', '/tutorial', 
                        '/dashboard', '/logout', '/'];
    
    const currentPath = window.location.pathname;
    const isPublicPage = publicPages.some(page => currentPath === page || currentPath.startsWith('/api/'));
    
    if (!isPublicPage) {
        // Verificar si el token existe
        if (!hasJwtToken()) {
            console.warn('⚠️ [JWT] Acceso denegado: Sin token de autenticación');
            window.location.href = '/login?error=unauthorized';
        } else if (isTokenExpired()) {
            console.warn('⚠️ [JWT] Token expirado, cerrando sesión');
            cerrarSesion();
        } else {
            console.log('✅ [JWT] Autenticación verificada para:', currentPath);
        }
    }
});

console.log('✅ [JWT Helper] Módulo JWT cargado correctamente');
