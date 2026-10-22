package com.limpiezait.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.limpiezait.api.dto.UsuarioDTO;

import com.limpiezait.api.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioDTO(u.getId(), u.getUsername(), u.getRol().name()))
                .toList();
    }

    @GetMapping("/me")
    public Map<String, Object> yo(@AuthenticationPrincipal UserDetails user) {
        return Map.of("username", user.getUsername(),
                      "roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }
}
