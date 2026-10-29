package com.limpiezait.api.service;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.limpiezait.api.dto.ProductoDTO;
import com.limpiezait.api.exception.ResourceNotFoundException;
import com.limpiezait.api.mapper.ProductoMapper;
import com.limpiezait.api.model.Producto;
import com.limpiezait.api.repository.ProductoRepository;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;
	private final CategoriaService categoriaService;

	public ProductoService(ProductoRepository productoRepository, CategoriaService categoriaService) {
		this.productoRepository = productoRepository;
		this.categoriaService = categoriaService;
	}

	public List<ProductoDTO> listar() {
		return productoRepository.findAll().stream().map(ProductoMapper::toDTO).toList();
	}

	public ProductoDTO buscarPorId(Long id) {
		Producto producto = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
		return ProductoMapper.toDTO(producto);
	}

	public ProductoDTO crear(ProductoDTO dto) {
		Producto producto = ProductoMapper.toEntity(dto);
		if (dto.categoriaId() != null) {
			producto.setCategoria(categoriaService.getEntityById(dto.categoriaId()));
		}
		return ProductoMapper.toDTO(productoRepository.save(producto));
	}

	public void eliminar(Long id) {
		if (!productoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
		}
		productoRepository.deleteById(id);

	}

	public void actualizar(Long id, ProductoDTO producto) {
		Producto productoExistente = productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
		productoExistente.setNombre(producto.nombre());
		productoExistente.setPrecio(producto.precio());
		productoExistente.setDescripcion(producto.descripcion());
		productoExistente.setUrlFoto(producto.urlFoto());
		if (producto.categoriaId() != null) {
			productoExistente.setCategoria(categoriaService.getEntityById(producto.categoriaId()));
		} else {
			productoExistente.setCategoria(null);
		}
		productoRepository.save(productoExistente);

	}

}
