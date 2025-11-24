/**
 * Sistema de Tema Claro/Oscuro para FoodIx
 * Gestiona el cambio entre modo claro y oscuro con persistencia en localStorage
 */

(function() {
    'use strict';

    // Obtener el tema guardado o usar 'light' por defecto
    const getStoredTheme = () => localStorage.getItem('theme') || 'light';
    const setStoredTheme = theme => localStorage.setItem('theme', theme);

    // Aplicar el tema al documento
    const applyTheme = (theme) => {
        document.documentElement.setAttribute('data-theme', theme);
        
        // Actualizar el estado visual del toggle (los iconos ya se manejan con CSS)
        const toggleBtn = document.querySelector('.theme-toggle');
        if (toggleBtn) {
            if (theme === 'dark') {
                toggleBtn.setAttribute('aria-label', 'Cambiar a modo claro');
            } else {
                toggleBtn.setAttribute('aria-label', 'Cambiar a modo oscuro');
            }
        }
    };

    // Alternar entre temas
    const toggleTheme = () => {
        const currentTheme = getStoredTheme();
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';
        
        setStoredTheme(newTheme);
        applyTheme(newTheme);
    };

    // Aplicar tema guardado al cargar la página
    document.addEventListener('DOMContentLoaded', () => {
        const storedTheme = getStoredTheme();
        applyTheme(storedTheme);
        
        // Agregar event listener al botón
        const toggleBtn = document.querySelector('.theme-toggle');
        if (toggleBtn) {
            toggleBtn.addEventListener('click', toggleTheme);
        }
    });

    // Aplicar tema inmediatamente (antes del DOMContentLoaded) para evitar flash
    applyTheme(getStoredTheme());
})();
