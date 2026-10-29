package com.limpiezait.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.limpiezait.api.config.JwtService;
import com.limpiezait.api.config.SecurityConfig;
import com.limpiezait.api.dto.ProductoDTO;
import com.limpiezait.api.exception.ResourceNotFoundException;
import com.limpiezait.api.service.ProductoService;

@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    // los necesita JwtAuthFilter; en este test no generamos tokens reales
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private static final String PRODUCTO_VALIDO = """
            {"nombre": "Antisarro", "precio": 2100.0, "descripcion": "Gatillo 500 ml", "url_foto": "antisarro.jpg"}
            """;

    @Test
    void listar_SinUsuario_Devuelve401() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void listar_ConUser_Devuelve200() throws Exception {
        when(productoService.listar()).thenReturn(List.of(
                new ProductoDTO(1L, "Lavandina 1L", 1500.0, "Uso general", null, null, null, null)));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Lavandina 1L"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void buscar_Inexistente_Devuelve404ConMensaje() throws Exception {
        when(productoService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Producto no encontrado con id: 99"));

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Producto no encontrado con id: 99"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void crear_ConUser_Devuelve403() throws Exception {
        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(PRODUCTO_VALIDO))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_ConAdmin_Devuelve201() throws Exception {
        when(productoService.crear(any())).thenReturn(
                new ProductoDTO(4L, "Antisarro", 2100.0, "Gatillo 500 ml", "antisarro.jpg", null, null, null));

        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(PRODUCTO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crear_Invalido_Devuelve400ConErroresPorCampo() throws Exception {
        String invalido = """
                {"nombre": "", "precio": -10, "descripcion": "x", "url_foto": "x.jpg"}
                """;

        mockMvc.perform(post("/api/productos").contentType(MediaType.APPLICATION_JSON).content(invalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").value("El nombre es obligatorio"))
                .andExpect(jsonPath("$.errores.precio").value("El precio debe ser mayor o igual a cero"));

        // si la validación falla, el service ni se entera
        verify(productoService, never()).crear(any());
    }

}
