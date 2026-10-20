package com.limpiezait.api.mapper;

import com.limpiezait.api.dto.CategoriaDTO;
import com.limpiezait.api.model.Categoria;

public final class CategoriaMapper {
	private CategoriaMapper() {
	}

	public static CategoriaDTO toDTO(Categoria c) {
		return new CategoriaDTO(c.getId(), c.getNombre());
	}
}