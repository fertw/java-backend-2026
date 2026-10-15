package com.limpiezait.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.limpiezait.api.model.Categoria;
import com.limpiezait.api.model.Producto;
import com.limpiezait.api.repository.CategoriaRepository;
import com.limpiezait.api.repository.ProductoRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public DataSeeder(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        Categoria pisos = categoriaRepository.save(new Categoria("Pisos"));
        Categoria banio = categoriaRepository.save(new Categoria("Baño"));

        productoRepository.save(nuevo("Lavandina 1L", 1500.0, "Uso general", banio));
        productoRepository.save(nuevo("Limpiador de pisos lavanda", 2300.0, "Botella 900 ml", pisos));
        productoRepository.save(nuevo("Desodorante de ambientes", 1900.0, "Aerosol 360 ml", banio));
    }

    private Producto nuevo(String nombre, Double precio, String descripcion, Categoria categoria) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescripcion(descripcion);
        p.setCategoria(categoria);
        return p;
    }
}
