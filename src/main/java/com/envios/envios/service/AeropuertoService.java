package com.envios.envios.service;

import com.envios.envios.exception.ResourceNotFoundException;
import com.envios.envios.model.Aeropuerto;
import com.envios.envios.repository.AeropuertoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AeropuertoService {

    private final AeropuertoRepository aeropuertoRepository;

    public AeropuertoService(AeropuertoRepository aeropuertoRepository) {
        this.aeropuertoRepository = aeropuertoRepository;
    }

    public List<Aeropuerto> getAllAeropuertos() {
        log.debug("Obteniendo todos los aeropuertos");
        List<Aeropuerto> aeropuertos = aeropuertoRepository.findAll();
        if (aeropuertos.isEmpty()) {
            log.error("No se encontraron aeropuertos ");
            throw new ResourceNotFoundException("No se encontraron aeropuertos ");
        }
        log.debug("Se encontraron {} aeropuertos", aeropuertos.size());
        return aeropuertos;
    }

    public Aeropuerto getAeropuertoById(Long id) {
        log.debug("Obteniendo aeropuerto con id: {}", id);
        return aeropuertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto no encontrado con id: " + id));
    }

    @Transactional
    public Aeropuerto createAeropuerto(Aeropuerto aeropuerto) {
        log.debug("Creando nuevo aeropuerto: {}", aeropuerto.getNombre());
        if (aeropuertoRepository.existsByNombre(aeropuerto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un aeropuerto con el nombre: " + aeropuerto.getNombre());
        }
        return aeropuertoRepository.save(aeropuerto);
    }

    @Transactional
    public Aeropuerto updateAeropuerto(Long id, Aeropuerto aeropuertoDetails) {
        log.debug("Actualizando aeropuerto con id: {}", id);
        Aeropuerto aeropuerto = getAeropuertoById(id);

        // Verificar si el nombre ya existe (excluyendo el actual)
        if (!aeropuerto.getNombre().equals(aeropuertoDetails.getNombre())) {
            if (aeropuertoRepository.existsByNombre(aeropuertoDetails.getNombre())) {
                throw new IllegalArgumentException(
                        "Ya existe un aeropuerto con el nombre: " + aeropuertoDetails.getNombre());
            }
        }

        aeropuerto.setNombre(aeropuertoDetails.getNombre());
        aeropuerto.setPais(aeropuertoDetails.getPais());
        aeropuerto.setCiudad(aeropuertoDetails.getCiudad());
        aeropuerto.setDireccion(aeropuertoDetails.getDireccion());

        return aeropuertoRepository.save(aeropuerto);

    }

    @Transactional
    public void deleteAeropuerto(Long id) {
        log.debug("Eliminando aeropuerto con id: {}", id);
        // Verificar si el aeropuerto existe
        Aeropuerto aeropuerto = getAeropuertoById(id);
        if (aeropuerto == null) {
            throw new ResourceNotFoundException("Aeropuerto no encontrado con id: " + id);
        }

        // Verificar si el aeropuerto está siendo usado en algún envío
        if (aeropuertoRepository.isAeropuertoUsedInEnvio(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar el aeropuerto porque está siendo utilizado en envíos");
        }

        aeropuertoRepository.delete(aeropuerto);
        log.debug("Aeropuerto con id: {} eliminado", id);
    }
}