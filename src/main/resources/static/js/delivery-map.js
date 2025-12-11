/**
 * Sistema de Mapa Interactivo para Selección de Dirección de Delivery
 * FoodIx - 2025
 */

let deliveryMap = null;
let deliveryMarker = null;
let userLatitude = -6.7714; // Chiclayo, Perú - Centro por defecto
let userLongitude = -79.8391;

// Inicializar el mapa cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', function() {
    initializeDeliveryMap();
    setupMapEventListeners();
});

/**
 * Inicializa el mapa de Leaflet
 */
function initializeDeliveryMap() {
    try {
        // Verificar si el contenedor del mapa existe
        const mapContainer = document.getElementById('deliveryMap');
        if (!mapContainer) {
            console.warn('⚠️ Contenedor del mapa no encontrado');
            return;
        }

        // Inicializar el mapa centrado en Chiclayo, Perú
        deliveryMap = L.map('deliveryMap').setView([userLatitude, userLongitude], 15);

        // Agregar capa de OpenStreetMap
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
        }).addTo(deliveryMap);

        // Crear icono personalizado para el marcador
        const customIcon = L.divIcon({
            className: 'custom-delivery-marker',
            html: '<div style="background: linear-gradient(135deg, #C62828 0%, #F5A623 100%); width: 30px; height: 30px; border-radius: 50% 50% 50% 0; transform: rotate(-45deg); border: 3px solid white; box-shadow: 0 4px 8px rgba(0,0,0,0.3); display: flex; align-items: center; justify-content: center;"><i class="fas fa-map-marker-alt" style="transform: rotate(45deg); color: white; font-size: 16px;"></i></div>',
            iconSize: [30, 30],
            iconAnchor: [15, 30]
        });

        // Agregar marcador inicial
        deliveryMarker = L.marker([userLatitude, userLongitude], {
            draggable: true,
            icon: customIcon
        }).addTo(deliveryMap);

        // Evento cuando se arrastra el marcador
        deliveryMarker.on('dragend', function(e) {
            const position = e.target.getLatLng();
            updateAddressFromCoordinates(position.lat, position.lng);
        });

        // Evento de clic en el mapa
        deliveryMap.on('click', function(e) {
            const lat = e.latlng.lat;
            const lng = e.latlng.lng;
            
            // Mover el marcador a la nueva posición
            deliveryMarker.setLatLng([lat, lng]);
            
            // Actualizar la dirección
            updateAddressFromCoordinates(lat, lng);
        });

        // Intentar obtener la ubicación actual del usuario
        tryGetUserLocation();

        console.log('✅ Mapa de delivery inicializado correctamente');
    } catch (error) {
        console.error('❌ Error al inicializar el mapa:', error);
    }
}

/**
 * Configura los event listeners para los controles del mapa
 */
function setupMapEventListeners() {
    // Botón para usar ubicación actual
    const btnUseMyLocation = document.getElementById('btnUseMyLocation');
    if (btnUseMyLocation) {
        btnUseMyLocation.addEventListener('click', function() {
            getUserLocation();
        });
    }

    // Event listener para cuando se muestra la sección del carrito
    document.querySelectorAll('.sidebar .nav-link').forEach(link => {
        link.addEventListener('click', function() {
            const section = this.getAttribute('href').substring(1);
            if (section === 'carrito') {
                // Pequeño delay para que el mapa se renderice correctamente
                setTimeout(() => {
                    if (deliveryMap) {
                        deliveryMap.invalidateSize();
                    }
                }, 300);
            }
        });
    });
}

/**
 * Intenta obtener la ubicación del usuario automáticamente
 */
function tryGetUserLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function(position) {
                const lat = position.coords.latitude;
                const lng = position.coords.longitude;
                
                userLatitude = lat;
                userLongitude = lng;
                
                // Actualizar mapa y marcador
                deliveryMap.setView([lat, lng], 16);
                deliveryMarker.setLatLng([lat, lng]);
                
                // Obtener dirección
                updateAddressFromCoordinates(lat, lng);
                
                console.log('✅ Ubicación del usuario obtenida:', lat, lng);
            },
            function(error) {
                console.log('ℹ️ No se pudo obtener la ubicación automáticamente:', error.message);
            },
            {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            }
        );
    }
}

/**
 * Obtiene la ubicación actual del usuario (cuando hace clic en el botón)
 */
function getUserLocation() {
    const btn = document.getElementById('btnUseMyLocation');
    
    if (!navigator.geolocation) {
        showNotification('Tu navegador no soporta geolocalización', 'error');
        return;
    }

    // Mostrar estado de carga
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Obteniendo ubicación...';

    navigator.geolocation.getCurrentPosition(
        function(position) {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;
            
            userLatitude = lat;
            userLongitude = lng;
            
            // Actualizar mapa y marcador con animación
            deliveryMap.flyTo([lat, lng], 17, {
                duration: 1.5
            });
            deliveryMarker.setLatLng([lat, lng]);
            
            // Obtener dirección
            updateAddressFromCoordinates(lat, lng);
            
            // Restaurar botón
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-crosshairs"></i> Usar mi ubicación actual';
            
            showNotification('✅ Ubicación obtenida correctamente', 'success');
        },
        function(error) {
            let errorMsg = 'Error al obtener la ubicación';
            
            switch(error.code) {
                case error.PERMISSION_DENIED:
                    errorMsg = 'Debes permitir el acceso a tu ubicación';
                    break;
                case error.POSITION_UNAVAILABLE:
                    errorMsg = 'Ubicación no disponible';
                    break;
                case error.TIMEOUT:
                    errorMsg = 'Tiempo de espera agotado';
                    break;
            }
            
            showNotification(errorMsg, 'error');
            
            // Restaurar botón
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-crosshairs"></i> Usar mi ubicación actual';
        },
        {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 0
        }
    );
}

/**
 * Convierte coordenadas a dirección usando Nominatim (OpenStreetMap)
 * @param {number} lat - Latitud
 * @param {number} lng - Longitud
 */
function updateAddressFromCoordinates(lat, lng) {
    console.log('🔍 Buscando dirección para:', lat, lng);
    
    // Actualizar coordenadas globales
    userLatitude = lat;
    userLongitude = lng;
    
    // Geocodificación inversa con Nominatim
    const url = `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}&zoom=18&addressdetails=1`;
    
    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data && data.display_name) {
                let address = data.display_name;
                
                // Intentar construir una dirección más legible
                if (data.address) {
                    const addr = data.address;
                    let parts = [];
                    
                    // Calle y número
                    if (addr.road) {
                        let street = addr.road;
                        if (addr.house_number) {
                            street += ' ' + addr.house_number;
                        }
                        parts.push(street);
                    }
                    
                    // Distrito/Barrio
                    if (addr.suburb || addr.neighbourhood) {
                        parts.push(addr.suburb || addr.neighbourhood);
                    }
                    
                    // Ciudad
                    if (addr.city || addr.town || addr.village) {
                        parts.push(addr.city || addr.town || addr.village);
                    }
                    
                    // Región
                    if (addr.state) {
                        parts.push(addr.state);
                    }
                    
                    if (parts.length > 0) {
                        address = parts.join(', ');
                    }
                }
                
                // Actualizar el campo de dirección
                const direccionInput = document.getElementById('direccionEntrega');
                if (direccionInput) {
                    direccionInput.value = address;
                    
                    // Efecto visual de actualización
                    direccionInput.style.background = 'rgba(245,166,35,0.1)';
                    setTimeout(() => {
                        direccionInput.style.background = '';
                    }, 500);
                }
                
                console.log('✅ Dirección encontrada:', address);
            }
        })
        .catch(error => {
            console.error('❌ Error al obtener dirección:', error);
            
            // Dirección por defecto con coordenadas
            const direccionInput = document.getElementById('direccionEntrega');
            if (direccionInput && !direccionInput.value) {
                direccionInput.value = `Lat: ${lat.toFixed(6)}, Lng: ${lng.toFixed(6)}`;
            }
        });
}

/**
 * Muestra una notificación temporal
 * @param {string} mensaje - Mensaje a mostrar
 * @param {string} tipo - Tipo: 'success' o 'error'
 */
function showNotification(mensaje, tipo) {
    const notif = document.createElement('div');
    notif.className = `alert alert-${tipo === 'success' ? 'success' : 'danger'} position-fixed`;
    notif.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px; box-shadow: 0 4px 12px rgba(0,0,0,0.2);';
    notif.innerHTML = `<i class="fas fa-${tipo === 'success' ? 'check-circle' : 'exclamation-circle'} me-2"></i>${mensaje}`;
    document.body.appendChild(notif);
    
    setTimeout(() => {
        notif.style.opacity = '0';
        notif.style.transition = 'opacity 0.3s ease';
        setTimeout(() => notif.remove(), 300);
    }, 3000);
}

/**
 * Obtiene las coordenadas actuales del marcador
 * @returns {Object} Objeto con lat y lng
 */
function getDeliveryCoordinates() {
    if (deliveryMarker) {
        const pos = deliveryMarker.getLatLng();
        return {
            latitude: pos.lat,
            longitude: pos.lng
        };
    }
    return {
        latitude: userLatitude,
        longitude: userLongitude
    };
}

// Exponer funciones globalmente si es necesario
window.getDeliveryCoordinates = getDeliveryCoordinates;
window.updateAddressFromCoordinates = updateAddressFromCoordinates;
