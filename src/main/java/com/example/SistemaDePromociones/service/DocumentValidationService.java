package com.example.SistemaDePromociones.service;

import com.example.SistemaDePromociones.dto.MiApiDniResponseDTO;
import com.example.SistemaDePromociones.dto.MiApiRucResponseDTO;
import com.example.SistemaDePromociones.dto.ReniecResponseDTO;
import com.example.SistemaDePromociones.dto.SunatResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Servicio para consultar DNI y RUC usando API externa
 */
@Service
public class DocumentValidationService {
    
    @Value("${api.reniec.base-url}")
    private String reniecBaseUrl;
    
    @Value("${api.sunat.base-url}")
    private String sunatBaseUrl;
    
    @Value("${api.token}")
    private String apiToken;
    
    private final RestTemplate restTemplate;
    
    public DocumentValidationService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Consulta un DNI en RENIEC
     * @param dni Número de DNI (8 dígitos)
     * @return ReniecResponseDTO con los datos de la persona
     */
    public ReniecResponseDTO consultarDNI(String dni) {
        try {
            System.out.println("🔍 [DNI] Consultando DNI: " + dni);
            
            // Validar que el DNI tenga 8 dígitos
            if (dni == null || !dni.matches("\\d{8}")) {
                throw new IllegalArgumentException("El DNI debe tener 8 dígitos");
            }
            
            // Construir URL (miapi.cloud usa /dni/{numero})
            String url = reniecBaseUrl + "/dni/" + dni;
            System.out.println("   URL: " + url);
            
            // Crear headers con el token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // DEBUG: Ver qué estamos enviando
            System.out.println("   Token (primeros 50 chars): " + apiToken.substring(0, Math.min(50, apiToken.length())));
            System.out.println("   Headers: " + headers);
            
            // Crear request entity
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // Hacer la petición (ahora usando MiApiDniResponseDTO)
            ResponseEntity<MiApiDniResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MiApiDniResponseDTO.class
            );
            
            MiApiDniResponseDTO miApiResponse = response.getBody();
            System.out.println("✅ [DNI] Respuesta exitosa: " + miApiResponse);
            
            // Convertir a ReniecResponseDTO para mantener compatibilidad con el controlador
            if (miApiResponse != null && miApiResponse.isSuccess()) {
                return miApiResponse.toReniecResponse();
            } else {
                throw new RuntimeException("La consulta no fue exitosa");
            }
            
        } catch (HttpClientErrorException e) {
            System.err.println("❌ [DNI] Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al consultar DNI: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ [DNI] Error general: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al consultar DNI: " + e.getMessage());
        }
    }
    
    /**
     * Consulta un RUC en SUNAT
     * @param ruc Número de RUC (11 dígitos)
     * @return SunatResponseDTO con los datos de la empresa
     */
    public SunatResponseDTO consultarRUC(String ruc) {
        try {
            System.out.println("🔍 [RUC] Consultando RUC: " + ruc);
            
            // Validar que el RUC tenga 11 dígitos
            if (ruc == null || !ruc.matches("\\d{11}")) {
                throw new IllegalArgumentException("El RUC debe tener 11 dígitos");
            }
            
            // Construir URL (miapi.cloud usa /ruc/completo/)
            String url = sunatBaseUrl + "/ruc/completo/" + ruc;
            System.out.println("   URL: " + url);
            
            // Crear headers con el token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Crear request entity
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            // Primero obtenemos la respuesta como String para ver qué llega
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
            );
            
            System.out.println("📥 [RUC] Respuesta RAW de MiAPI: " + rawResponse.getBody());
            
            // Ahora intentamos mapear a MiApiRucResponseDTO
            ResponseEntity<MiApiRucResponseDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MiApiRucResponseDTO.class
            );
            
            MiApiRucResponseDTO miApiResponse = response.getBody();
            System.out.println("📥 [RUC] Respuesta parseada de MiAPI: " + miApiResponse);
            
            // Verificar que la respuesta sea exitosa
            if (miApiResponse == null || !miApiResponse.isSuccess() || miApiResponse.getDatos() == null) {
                System.err.println("❌ [RUC] Respuesta inválida: success=" + (miApiResponse != null ? miApiResponse.isSuccess() : "null") + 
                                 ", datos=" + (miApiResponse != null ? miApiResponse.getDatos() : "null"));
                throw new RuntimeException("RUC no encontrado o respuesta inválida");
            }
            
            // Convertir a SunatResponseDTO
            SunatResponseDTO data = miApiResponse.toSunatResponse();
            System.out.println("✅ [RUC] Datos procesados: " + data);
            
            return data;
            
        } catch (HttpClientErrorException e) {
            System.err.println("❌ [RUC] Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al consultar RUC: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ [RUC] Error general: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al consultar RUC: " + e.getMessage());
        }
    }
}
