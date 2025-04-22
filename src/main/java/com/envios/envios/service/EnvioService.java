package com.envios.envios.service;

import com.envios.envios.exception.ResourceNotFoundException;
import com.envios.envios.model.Aeropuerto;
import com.envios.envios.model.Envio;
import com.envios.envios.repository.AeropuertoRepository;
import com.envios.envios.repository.EnvioRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final AeropuertoRepository aeropuertoRepository;

    public EnvioService(EnvioRepository envioRepository, AeropuertoRepository aeropuertoRepository) {
        this.envioRepository = envioRepository;
        this.aeropuertoRepository = aeropuertoRepository;
    }

    public List<Envio> getAllEnvios() {
        log.debug("Obteniendo todos los envíos");
        return envioRepository.findAll();
    }

    public Envio getEnvioById(Long id) {
        log.debug("Obteniendo envío con id: {}", id);
        return envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío no encontrado con id: " + id));
    }

    @Transactional
    public Envio createEnvio(Envio envio) {
        log.debug("Creando nuevo envío: {}", envio.getDescripcion());
        if (envio.getOrigen().getId().equals(envio.getDestino().getId())) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser el mismo aeropuerto");
        }

        aeropuertoRepository.findById(envio.getOrigen().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aeropuerto origen no encontrado con id: " + envio.getOrigen().getId()));

        aeropuertoRepository.findById(envio.getDestino().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aeropuerto destino no encontrado con id: " + envio.getDestino().getId()));

        return envioRepository.save(envio);
    }

    @Transactional
    public Envio updateEnvio(Long id, Envio envioDetails) {
        Envio envio = getEnvioById(id);

        // Validar que los aeropuertos no sean nulos
        if (envioDetails.getOrigen() == null || envioDetails.getDestino() == null) {
            throw new IllegalArgumentException("Origen y destino son obligatorios");
        }

        // Validar que no sean el mismo aeropuerto
        if (envioDetails.getOrigen().getId().equals(envioDetails.getDestino().getId())) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser el mismo aeropuerto");
        }

        // Verificar si los aeropuertos existen
        Aeropuerto origen = aeropuertoRepository.findById(envioDetails.getOrigen().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aeropuerto origen no encontrado con id: " + envioDetails.getOrigen().getId()));

        Aeropuerto destino = aeropuertoRepository.findById(envioDetails.getDestino().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Aeropuerto destino no encontrado con id: " + envioDetails.getDestino().getId()));

        // Actualizar los campos
        envio.setOrigen(origen);
        envio.setDestino(destino);
        envio.setDescripcion(envioDetails.getDescripcion());

        return envioRepository.save(envio);
    }

    @Transactional
    public void deleteEnvio(Long id) {
        log.debug("Eliminando envío con id: {}", id);
        Envio envio = getEnvioById(id);
        envioRepository.delete(envio);
        // Verificar si el envío fue eliminado correctamente
        if (envioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se pudo eliminar el envío con id: " + id);
        }
        log.debug("Envío con id: {} eliminado", id);
    }
}