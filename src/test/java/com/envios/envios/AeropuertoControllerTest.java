package com.envios.envios;

import com.envios.envios.model.Aeropuerto;
import com.envios.envios.service.AeropuertoService;
import com.envios.envios.controller.AeropuertoController;
import com.envios.envios.hateoas.AeropuertoModelAssembler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AeropuertoController.class)
public class AeropuertoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean

    private AeropuertoService aeropuertoService;
    @SuppressWarnings("removal")
    @MockBean
    private AeropuertoModelAssembler aeropuertoModelAssembler;

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })

    public void testGetAeropuertoById() throws Exception {

        // Datos de prueba
        Aeropuerto aeropuerto = new Aeropuerto();
        aeropuerto.setId(1L);
        aeropuerto.setNombre("Aeropuerto Internacional");
        aeropuerto.setPais("España");
        aeropuerto.setCiudad("Madrid");
        aeropuerto.setDireccion("Calle Falsa 123");

        EntityModel<Aeropuerto> entityModel = EntityModel.of(aeropuerto,
                linkTo(methodOn(AeropuertoController.class).getAeropuertoById(1L)).withSelfRel(),
                linkTo(methodOn(AeropuertoController.class).getAllAeropuertos()).withRel("all"),
                linkTo(methodOn(AeropuertoController.class).createAeropuerto(aeropuerto)).withRel("create"),
                linkTo(methodOn(AeropuertoController.class).updateAeropuerto(1L, aeropuerto)).withRel("update"),
                linkTo(methodOn(AeropuertoController.class).deleteAeropuerto(1L)).withRel("delete"));

        // Simulación del servicio y el assembler
        when(aeropuertoService.getAeropuertoById(1L)).thenReturn(aeropuerto);
        when(aeropuertoModelAssembler.toModel(aeropuerto)).thenReturn(entityModel);

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/api/aeropuertos/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Aeropuerto Internacional"))
                .andExpect(jsonPath("$.pais").value("España"))
                .andExpect(jsonPath("$.ciudad").value("Madrid"))
                .andExpect(jsonPath("$.direccion").value("Calle Falsa 123"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists());

    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testCreateAeropuerto_Success() throws Exception {

        Aeropuerto inputAeropuerto = new Aeropuerto();
        inputAeropuerto.setNombre("Aeropuerto Barajas");
        inputAeropuerto.setPais("España");
        inputAeropuerto.setCiudad("Madrid");
        inputAeropuerto.setDireccion("Av. de la Hispanidad, s/n");

        Aeropuerto savedAeropuerto = new Aeropuerto();
        savedAeropuerto.setId(1L);
        savedAeropuerto.setNombre(inputAeropuerto.getNombre());
        savedAeropuerto.setPais(inputAeropuerto.getPais());
        savedAeropuerto.setCiudad(inputAeropuerto.getCiudad());
        savedAeropuerto.setDireccion(inputAeropuerto.getDireccion());

        when(aeropuertoService.createAeropuerto(any(Aeropuerto.class))).thenReturn(savedAeropuerto);

        EntityModel<Aeropuerto> entityModel = EntityModel.of(savedAeropuerto,
                linkTo(methodOn(AeropuertoController.class).getAeropuertoById(1L)).withSelfRel(),
                linkTo(methodOn(AeropuertoController.class).getAllAeropuertos()).withRel("all"),
                linkTo(methodOn(AeropuertoController.class).createAeropuerto(inputAeropuerto)).withRel("create"),
                linkTo(methodOn(AeropuertoController.class).updateAeropuerto(1L, savedAeropuerto)).withRel("update"),
                linkTo(methodOn(AeropuertoController.class).deleteAeropuerto(1L)).withRel("delete"));

        when(aeropuertoModelAssembler.toModel(savedAeropuerto)).thenReturn(entityModel);

        mockMvc.perform(post("/api/aeropuertos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)

                .content(objectMapper.writeValueAsString(inputAeropuerto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Aeropuerto Barajas"))
                .andExpect(jsonPath("$.pais").value("España"))
                .andExpect(jsonPath("$.ciudad").value("Madrid"))
                .andExpect(jsonPath("$.direccion").value("Av. de la Hispanidad, s/n"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.all.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testCreateAeropuerto_ValidationFailed() throws Exception {
        // Arrange - Aeropuerto sin campos obligatorios
        Aeropuerto invalidAeropuerto = new Aeropuerto();
        invalidAeropuerto.setNombre(""); // Nombre vacío no permitido

        // Act & Assert
        mockMvc.perform(post("/api/aeropuertos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAeropuerto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin123", roles = { "ADMIN" })
    public void testCreateAeropuerto_ServiceException() throws Exception {
        // Arrange - Datos de entrada válidos
        Aeropuerto inputAeropuerto = new Aeropuerto();
        inputAeropuerto.setNombre("Aeropuerto Duplicado");
        inputAeropuerto.setPais("España");
        inputAeropuerto.setCiudad("Barcelona");
        inputAeropuerto.setDireccion("Calle Ejemplo 123");

        // Arrange - Configuración del mock para lanzar excepción
        when(aeropuertoService.createAeropuerto(any(Aeropuerto.class)))
                .thenThrow(new IllegalArgumentException("El aeropuerto ya existe"));

        // Act & Assert
        mockMvc.perform(post("/api/aeropuertos")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputAeropuerto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El aeropuerto ya existe"));
    }
}