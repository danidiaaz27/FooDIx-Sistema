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
        
        // Actualizar el ícono del botón
        const sunIcon = document.querySelector('.theme-toggle .fa-sun');
        const moonIcon = document.querySelector('.theme-toggle .fa-moon');
        
        if (sunIcon && moonIcon) {
            if (theme === 'dark') {
                sunIcon.style.display = 'none';
                moonIcon.style.display = 'inline-block';
            } else {
                sunIcon.style.display = 'inline-block';
                moonIcon.style.display = 'none';
            }
        }
    };

    // Alternar entre temas
    const toggleTheme = () => {
        const currentTheme = getStoredTheme();
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';
        
        setStoredTheme(newTheme);
        applyTheme(newTheme);
        
        // Animación suave del botón
        const toggleBtn = document.querySelector('.theme-toggle');
        if (toggleBtn) {
            toggleBtn.style.transform = 'scale(1.2) rotate(360deg)';
            setTimeout(() => {
                toggleBtn.style.transform = '';
            }, 300);
        }
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
