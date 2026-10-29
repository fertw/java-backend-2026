package com.limpiezait.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.limpiezait.api.model.Producto;

@DataJpaTest
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void cargarDatos() {
        // id en null: lo genera la base (con un id fijo, Hibernate intenta un merge y falla)
        productoRepository.save(new Producto(null, "Producto 1", 10.0, "Descripción del producto 1", "url1"));
        productoRepository.save(new Producto(null, "Producto 2", 20.0, "Descripción del producto 2", "url2"));
        productoRepository.save(new Producto(null, "Detergente", 30.0, "Limón 750 ml", "url3"));
    }

    @Test
    void findByNombreContainingIgnoreCase_ShouldReturnMatchingProductos() {
        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase("PRODUCTO");

        assertThat(productos).hasSize(2)
                .extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Producto 1", "Producto 2");
    }

    @Test
    void save_ShouldCompletarFechaAltaConPrePersist() {
        Producto guardado = productoRepository.save(new Producto(null, "Esponja", 500.0, "Doble faz", "url4"));

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getFechaAlta()).isNotNull();
    }

}
