package com.limpiezait.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.limpiezait.api.model.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
	Usuario findByUsername(String username);

}
