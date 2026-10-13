package com.limpiezait.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limpiezait.api.model.Producto;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	private final List<Producto> productos = new ArrayList<>();
	private final AtomicLong secuencia = new AtomicLong();

	@GetMapping
	public List<Producto> getAllProductos() {
		return productos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
		return productos.stream()
				.filter(p -> p.getId().equals(id))
				.findFirst()
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
		producto.setId(secuencia.incrementAndGet());
		productos.add(producto);
		return ResponseEntity.status(HttpStatus.CREATED).body(producto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto datos) {
		for (Producto p : productos) {
			if (p.getId().equals(id)) {
				p.setNombre(datos.getNombre());
				p.setPrecio(datos.getPrecio());
				p.setDescripcion(datos.getDescripcion());
				p.setUrlFoto(datos.getUrlFoto());
				return ResponseEntity.ok(p);
			}
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
		boolean borrado = productos.removeIf(p -> p.getId().equals(id));
		return borrado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

}
