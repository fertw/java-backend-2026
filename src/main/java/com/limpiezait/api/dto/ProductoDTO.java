package com.limpiezait.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductoDTO(
        Long id,
        String nombre,
        Double precio,
        String descripcion,
        @JsonProperty("url_foto") String urlFoto,
        @JsonProperty("fecha_alta") @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime fechaAlta,
        @JsonProperty("categoria_id") Long categoriaId,
        String categoria) {
}