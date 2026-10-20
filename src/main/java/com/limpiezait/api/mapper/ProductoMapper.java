package com.limpiezait.api.mapper;

import com.limpiezait.api.dto.ProductoDTO;
import com.limpiezait.api.model.Categoria;
import com.limpiezait.api.model.Producto;

public final class ProductoMapper {

    private ProductoMapper() { }

    public static ProductoDTO toDTO(Producto p) {
        Categoria c = p.getCategoria();
        return new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio(), p.getDescripcion(),
                p.getUrlFoto(), p.getFechaAlta(),
                c != null ? c.getId() : null,
                c != null ? c.getNombre() : null);
    }

    public static Producto toEntity(ProductoDTO dto) {
        Producto p = new Producto();
        p.setNombre(dto.nombre());
        p.setPrecio(dto.precio());
        p.setDescripcion(dto.descripcion());
        p.setUrlFoto(dto.urlFoto());
        return p;
    }
}
