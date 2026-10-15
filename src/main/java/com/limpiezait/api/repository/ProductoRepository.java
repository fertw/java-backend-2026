package com.limpiezait.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.limpiezait.api.model.Producto;

@Repository
public class ProductoRepository {

	private final List<Producto> productos = new ArrayList<>();
	private final AtomicLong secuencia = new AtomicLong();

	public List<Producto> findAll() {
		return new ArrayList<>(productos);
	}

	public Optional<Producto> findById(Long id) {
		return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
	}

	public Producto save(Producto producto) {
		if (producto.getId() == null) {
			producto.setId(secuencia.incrementAndGet());
			productos.add(producto);
		}
		return producto;
	}

	public void deleteById(Long id) {
		productos.removeIf(p -> p.getId().equals(id));
	}

}
