package com.limpiezait.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.limpiezait.api.dto.ProductoDTO;
import com.limpiezait.api.exception.ResourceNotFoundException;
import com.limpiezait.api.model.Categoria;
import com.limpiezait.api.model.Producto;
import com.limpiezait.api.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void buscarPorId_ProductoExistente_RetornaProductoDTO() {
        // Arrange
        Producto lavandina = new Producto(1L, "Lavandina 1L", 1500.0, "Lavandina de 1 litro", "url_foto");
        when(productoRepository.findById(1L)).thenReturn(Optional.of(lavandina));

        // Act
        ProductoDTO dto = productoService.buscarPorId(1L);

        // Assert
        assertThat(dto.nombre()).isEqualTo("Lavandina 1L");
        assertThat(dto.precio()).isEqualTo(1500.0);
    }

    @Test
    void buscarPorId_ProductoInexistente_LanzaResourceNotFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void crear_ConCategoria_LaAsignaAntesDeGuardar() {
        Categoria banio = new Categoria("Baño");
        when(categoriaService.getEntityById(2L)).thenReturn(banio);
        // el "save" del mock devuelve el mismo producto que recibe
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoDTO nuevo = new ProductoDTO(null, "Antisarro", 2100.0, "Gatillo 500 ml", "antisarro.jpg", null, 2L, null);
        ProductoDTO creado = productoService.crear(nuevo);

        assertThat(creado.categoria()).isEqualTo("Baño");
    }

    @Test
    void eliminar_ProductoInexistente_NoBorraNada() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> productoService.eliminar(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(productoRepository, never()).deleteById(any());
    }

}
