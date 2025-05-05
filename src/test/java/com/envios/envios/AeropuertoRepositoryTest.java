package com.envios.envios;

import com.envios.envios.model.Aeropuerto;
import com.envios.envios.repository.AeropuertoRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AeropuertoRepositoryTest {

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    @Test
    public void testGuardarYBuscar() {
        Aeropuerto aeropuerto = new Aeropuerto();
        aeropuerto.setNombre("Aeropuerto Internacional");
        aeropuerto.setPais("España");
        aeropuerto.setCiudad("Madrid");
        aeropuerto.setDireccion("Calle Falsa 123");

        Aeropuerto saved = aeropuertoRepository.save(aeropuerto);

        Optional<Aeropuerto> found = aeropuertoRepository.findById(saved.getId()); // Usa el ID generado

        assertTrue(found.isPresent());
        assertEquals("Aeropuerto Internacional", found.get().getNombre());
    }
}
