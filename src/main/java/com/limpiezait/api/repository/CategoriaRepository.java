package com.limpiezait.api.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.limpiezait.api.model.Categoria;


public interface CategoriaRepository  extends JpaRepository<Categoria, Long> {
	// Aquí puedes agregar métodos de consulta personalizados si es necesario

}
