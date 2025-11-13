package com.example.SistemaDePromociones.security;

import com.example.SistemaDePromociones.model.Rol;
import com.example.SistemaDePromociones.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de UserDetails para Spring Security
 * Utiliza las constantes de Rol para mapear correctamente los roles
 */
public class CustomUserDetails implements UserDetails {
    
    private final Usuario usuario;
    
    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapear el código de rol a un authority usando las constantes de Rol
        String role = getRoleFromCodigo(usuario.getCodigoRol());
        System.out.println("🔑 [AUTH] Asignando autoridad: ROLE_" + role + " para usuario: " + usuario.getCorreoElectronico());
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
    
    /**
     * Convierte el código de rol a su representación en Spring Security
     * Utiliza las constantes definidas en la entidad Rol
     */
    private String getRoleFromCodigo(Long codigoRol) {
        if (Rol.ADMINISTRADOR.equals(codigoRol)) {
            return "ADMIN";
        } else if (Rol.RESTAURANTE.equals(codigoRol)) {
            return "RESTAURANT";
        } else if (Rol.REPARTIDOR.equals(codigoRol)) {
            return "DELIVERY";
        } else if (Rol.USUARIO.equals(codigoRol)) {
            return "CUSTOMER";
        }
        return "CUSTOMER"; // Fallback
    }
    
    @Override
    public String getPassword() {
        return usuario.getContrasena();
    }
    
    @Override
    public String getUsername() {
        return usuario.getCorreoElectronico();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        // Si el estado es null, considerar como habilitado por defecto
        return usuario.getEstado() != null ? usuario.getEstado() : true;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Long getCodigoRol() {
        return usuario.getCodigoRol();
    }
}
