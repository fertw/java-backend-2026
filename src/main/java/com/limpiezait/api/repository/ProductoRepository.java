package com.limpiezait.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.limpiezait.api.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	// Aquí puedes agregar métodos de consulta personalizados si es necesario
	
    List<Producto> findByNombreContainingIgnoreCase(String nombre);




}
