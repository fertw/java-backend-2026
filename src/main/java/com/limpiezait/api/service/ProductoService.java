package com.limpiezait.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.limpiezait.api.exception.ResourceNotFoundException;
import com.limpiezait.api.model.Producto;
import com.limpiezait.api.repository.ProductoRepository;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	public List<Producto> listar() {
		return productoRepository.findAll();
	}

	public Producto buscarPorId(Long id) {
		return productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto " + id + " no encontrado"));
	}

	public Producto crear(Producto producto) {
		validar(producto);
		// el id lo asigna el servidor, aunque el cliente mande uno
		producto.setId(null);
		return productoRepository.save(producto);
	}

	public Producto actualizar(Long id, Producto datos) {
		validar(datos);
		Producto existente = buscarPorId(id);
		existente.setNombre(datos.getNombre());
		existente.setPrecio(datos.getPrecio());
		existente.setDescripcion(datos.getDescripcion());
		existente.setUrlFoto(datos.getUrlFoto());
		return productoRepository.save(existente);
	}

	public void eliminar(Long id) {
		// si no existe, lanza 404 en lugar de devolver un 204 silencioso
		buscarPorId(id);
		productoRepository.deleteById(id);
	}

	private void validar(Producto p) {
		if (p.getNombre() == null || p.getNombre().isBlank()) {
			throw new IllegalArgumentException("El nombre es obligatorio");
		}
		if (p.getPrecio() == null || p.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor a 0");
		}
	}

}
