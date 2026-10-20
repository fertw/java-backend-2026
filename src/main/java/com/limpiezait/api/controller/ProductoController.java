package com.limpiezait.api.controller;

import java.util.List;

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

import com.limpiezait.api.dto.ProductoDTO;
import com.limpiezait.api.model.Producto;
import com.limpiezait.api.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@GetMapping
	public List<ProductoDTO> getAllProductos() {
		return productoService.listar();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
		return ResponseEntity.ok(productoService.buscarPorId(id));
	}

	@PostMapping
	public ResponseEntity<ProductoDTO> createProducto(@RequestBody ProductoDTO producto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(producto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoDTO producto) {
		productoService.actualizar(id, producto);
		return ResponseEntity.ok(productoService.buscarPorId(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
		productoService.eliminar(id);
		return ResponseEntity.noContent().build();
	}

}
