package com.limpiezait.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Producto de limpieza")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductoDTO(
                @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "1") Long id,
                @Schema(example = "Lavandina 1L") String nombre,
                @Schema(example = "1500.0") Double precio,
                @Schema(example = "Uso general") String descripcion,
                @JsonProperty("url_foto") String urlFoto,
                @Schema(accessMode = Schema.AccessMode.READ_ONLY) @JsonProperty("fecha_alta") @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime fechaAlta,
                @JsonProperty("categoria_id") @Schema(example = "1") Long categoriaId,
                @Schema(accessMode = Schema.AccessMode.READ_ONLY) String categoria) {
}
