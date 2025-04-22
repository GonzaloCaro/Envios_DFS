package com.envios.envios.controller;

import com.envios.envios.exception.ResourceNotFoundException;
import com.envios.envios.model.ResponseWrapper;
import com.envios.envios.model.Aeropuerto;
import com.envios.envios.service.AeropuertoService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/aeropuertos")
public class AeropuertoController {

    private final AeropuertoService aeropuertoService;

    public AeropuertoController(AeropuertoService aeropuertoService) {
        this.aeropuertoService = aeropuertoService;
    }

    @GetMapping
    public ResponseEntity<?> getAllAeropuertos() {
        log.debug("Controller: Obteniendo todos los aeropuertos");
        List<Aeropuerto> aeropuertos = aeropuertoService.getAllAeropuertos();
        if (aeropuertos.isEmpty()) {
            log.error("Controller: No se encontraron aeropuertos");
            throw new ResourceNotFoundException("No se encontraron aeropuertos");
        }
        log.debug("Controller: Se encontraron {} aeropuertos", aeropuertos.size());
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        "OK",
                        aeropuertos.size(),
                        aeropuertos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAeropuertoById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo aeropuerto con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de aeropuerto no puede ser nulo");
            throw new IllegalArgumentException("ID de aeropuerto no puede ser nulo");
        }
        try {
            Aeropuerto aeropuerto = aeropuertoService.getAeropuertoById(id);
            return ResponseEntity.ok(aeropuerto);
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Aeropuerto no encontrado con id: {}", id);
            throw e;
        }
    }

    // Crear un nuevo aeropuerto
    @PostMapping
    public ResponseEntity<ResponseWrapper<Aeropuerto>> createAeropuerto(@RequestBody Aeropuerto aeropuerto) {
        log.debug("Controller: Creando nuevo aeropuerto: {}", aeropuerto.getNombre());
        try {
            Aeropuerto createdAeropuerto = aeropuertoService.createAeropuerto(aeropuerto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseWrapper<>(
                            "Aeropuerto creado exitosamente",
                            1,
                            List.of(createdAeropuerto)));
        } catch (IllegalArgumentException e) {
            log.error("Controller: Error al crear aeropuerto: {}", e.getMessage());
            throw e;
        }
    }

    // Actualizar un aeropuerto existente
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Aeropuerto>> updateAeropuerto(@PathVariable Long id,
            @RequestBody Aeropuerto aeropuertoDetails) {
        log.debug("Controller: Actualizando aeropuerto con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de aeropuerto no puede ser nulo");
            throw new IllegalArgumentException("ID de aeropuerto no puede ser nulo");
        }
        try {
            Aeropuerto updatedAeropuerto = aeropuertoService.updateAeropuerto(id, aeropuertoDetails);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Aeropuerto actualizado exitosamente",
                            1,
                            List.of(updatedAeropuerto)));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAeropuerto(@PathVariable Long id) {
        log.debug("Controller: Eliminando aeropuerto con id: {}", id);
        try {
            aeropuertoService.deleteAeropuerto(id);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Aeropuerto eliminado exitosamente",
                            0,
                            null));
        } catch (ResourceNotFoundException | IllegalStateException e) {
            throw e;
        }
    }
}