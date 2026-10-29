package com.limpiezait.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Producto de limpieza")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductoDTO(
        @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "1") Long id,
        @NotBlank(message = "El nombre es obligatorio") @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres") @Schema(example = "Lavandina 1L") String nombre,

        @NotNull(message = "El precio es obligatorio") @PositiveOrZero(message = "El precio debe ser mayor o igual a cero") @Schema(example = "1500.0") Double precio,

        @NotBlank(message = "La descripción es obligatoria") @Schema(example = "Uso general") String descripcion,

        @NotBlank(message = "La URL de la foto es obligatoria") @JsonProperty("url_foto") String urlFoto,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) @JsonProperty("fecha_alta") @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime fechaAlta,
        @JsonProperty("categoria_id") @Schema(example = "1") Long categoriaId,
        @Schema(accessMode = Schema.AccessMode.READ_ONLY) String categoria) {
}
