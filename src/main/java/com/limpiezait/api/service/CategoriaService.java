package com.limpiezait.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.limpiezait.api.dto.CategoriaDTO;
import com.limpiezait.api.exception.ResourceNotFoundException;
import com.limpiezait.api.mapper.CategoriaMapper;
import com.limpiezait.api.model.Categoria;
import com.limpiezait.api.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream().map(CategoriaMapper::toDTO).toList();
    }
    
    public Categoria getEntityById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría " + id + " no encontrada"));
    }
       

    public CategoriaDTO crear(CategoriaDTO dto) {
        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return CategoriaMapper.toDTO(categoriaRepository.save(new Categoria(dto.nombre())));
    }
}
