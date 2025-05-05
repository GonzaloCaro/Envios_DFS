package com.envios.envios;

import com.envios.envios.service.AeropuertoService;
import com.envios.envios.model.Aeropuerto;
import com.envios.envios.repository.AeropuertoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AeropuertoServiceTest {

    private AeropuertoService aeropuertoService;

    private AeropuertoRepository aeropuertoRepository;

    @BeforeEach
    public void setUp() {
        aeropuertoRepository = mock(AeropuertoRepository.class);
        aeropuertoService = new AeropuertoService(aeropuertoRepository);
    }

    @Test
    public void testGetAllAeropuertos() {
        // Datos de prueba
        Aeropuerto aeropuerto1 = new Aeropuerto();
        aeropuerto1.setId(1L);
        aeropuerto1.setPais("Pais 1");
        aeropuerto1.setNombre("Aeropuerto 1");
        aeropuerto1.setCiudad("Ciudad 1");
        aeropuerto1.setDireccion("Direccion 1");
        
        Aeropuerto aeropuerto2 = new Aeropuerto();
        aeropuerto2.setId(2L);
        aeropuerto2.setPais("Pais 2");
        aeropuerto2.setNombre("Aeropuerto 2");
        aeropuerto2.setCiudad("Ciudad 2");
        aeropuerto2.setDireccion("Direccion 2");

        List<Aeropuerto> aeropuertos = Arrays.asList(aeropuerto1, aeropuerto2);

        // Configurar el comportamiento del mock
        when(aeropuertoRepository.findAll()).thenReturn(aeropuertos);

        // Llamar al método a probar
        List<Aeropuerto> result = aeropuertoService.getAllAeropuertos();

        // Verificar resultados
        assertEquals(2, result.size());
        assertEquals("Aeropuerto 1", result.get(0).getNombre());
        assertEquals("Aeropuerto 2", result.get(1).getNombre());
    }

    @Test
    public void testGetAeropuertoById() {
        // Datos de prueba
        Aeropuerto aeropuerto = new Aeropuerto();
        aeropuerto.setId(1L);
        aeropuerto.setPais("Pais 1");
        aeropuerto.setNombre("Aeropuerto 1");
        aeropuerto.setCiudad("Ciudad 1");
        aeropuerto.setDireccion("Direccion 1");

        // Configurar el comportamiento del mock
        when(aeropuertoRepository.findById(1L)).thenReturn(Optional.of(aeropuerto));

        // Llamar al método a probar
        Aeropuerto result = aeropuertoService.getAeropuertoById(1L);

        // Verificar resultados
        assertEquals("Aeropuerto 1", result.getNombre());
    }
}