package com.example.SistemaDePromociones.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class VerificationService {
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Long> codeExpiration = new ConcurrentHashMap<>();
    private final Random random = new Random();
    
    private static final long CODE_EXPIRATION_MS = 10 * 60 * 1000; // 10 minutos

    public String generateCode(String email) {
        String code = String.format("%06d", random.nextInt(1000000));
        verificationCodes.put(email, code);
        codeExpiration.put(email, System.currentTimeMillis() + CODE_EXPIRATION_MS);
        System.out.println("🔐 Código generado para " + email + ": " + code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        Long expirationTime = codeExpiration.get(email);
        
        if (storedCode == null || expirationTime == null) {
            System.out.println("❌ No hay código almacenado para: " + email);
            return false;
        }
        
        if (System.currentTimeMillis() > expirationTime) {
            System.out.println("❌ Código expirado para: " + email);
            verificationCodes.remove(email);
            codeExpiration.remove(email);
            return false;
        }
        
        boolean isValid = storedCode.equals(code);
        System.out.println("🔍 Verificando código para " + email + 
                          " - Esperado: " + storedCode + 
                          " - Recibido: " + code + 
                          " - Válido: " + isValid);
        
        if (isValid) {
            verificationCodes.remove(email);
            codeExpiration.remove(email);
        }
        
        return isValid;
    }

    public void removeCode(String email) {
        verificationCodes.remove(email);
        codeExpiration.remove(email);
    }
}