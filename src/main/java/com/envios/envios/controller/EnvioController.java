package com.envios.envios.controller;

import com.envios.envios.model.Envio;
import com.envios.envios.model.ResponseWrapper;
import com.envios.envios.service.EnvioService;
import com.envios.envios.DTO.EnvioDTO;
import com.envios.envios.exception.ResourceNotFoundException;
import com.envios.envios.mapper.EnvioMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;
    private final EnvioMapper envioMapper;

    public EnvioController(EnvioService envioService, EnvioMapper envioMapper) {
        this.envioService = envioService;
        this.envioMapper = envioMapper;
    }

    @GetMapping
    public ResponseEntity<?> getAllEnvios() {
        log.debug("Controller: Obteniendo todos los envíos");
        List<Envio> envios = envioService.getAllEnvios();
        if (envios.isEmpty()) {
            log.error("Controller: No se encontraron envíos");
            throw new ResourceNotFoundException("No se encontraron envíos");
        }
        log.debug("Controller: Se encontraron {} envíos", envios.size());
        return ResponseEntity.ok(
                new ResponseWrapper<>(
                        "OK",
                        envios.size(),
                        envios));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEnvioById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo envío con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de envío no puede ser nulo");
            throw new IllegalArgumentException("ID de envío no puede ser nulo");
        }
        try {
            Envio envio = envioService.getEnvioById(id);
            return ResponseEntity.ok(envio);
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Envío no encontrado con id: {}", id);
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Envio>> createEnvio(@RequestBody EnvioDTO envioDTO) {
        log.debug("Controller: Creando nuevo envío");
        if (envioDTO == null) {
            log.error("Controller: Envío no puede ser nulo");
            throw new IllegalArgumentException("Envío no puede ser nulo");
        }
        try {
            Envio envio = envioMapper.toEntity(envioDTO);
            Envio createdEnvio = envioService.createEnvio(envio);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseWrapper<>(
                            "Envío creado exitosamente",
                            1,
                            List.of(createdEnvio)));
        } catch (RuntimeException e) {
            log.error("Controller: Error al crear el envío: {}", e.getMessage());
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnvio(@PathVariable Long id, @RequestBody EnvioDTO envioDTO) {
        log.debug("Controller: Actualizando envío con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de envío no puede ser nulo");
            throw new IllegalArgumentException("ID de envío no puede ser nulo");
        }
        try {
            // Convertir DTO a Entidad
            Envio envioDetails = envioMapper.toEntity(envioDTO);

            // Realizar la actualización
            Envio updatedEnvio = envioService.updateEnvio(id, envioDetails);

            // Convertir a DTO para la respuesta
            EnvioDTO responseDto = envioMapper.toDto(updatedEnvio);

            log.debug("Controller: Envío con id: {} actualizado exitosamente", id);
            // Retornar la respuesta
            return ResponseEntity.ok(responseDto);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnvio(@PathVariable Long id) {
        log.debug("Controller: Eliminando envío con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de envío no puede ser nulo");
            throw new IllegalArgumentException("ID de envío no puede ser nulo");
        }
        try {
            envioService.deleteEnvio(id);
            log.debug("Controller: Envío con id: {} eliminado exitosamente", id);
            return ResponseEntity.ok(
                    new ResponseWrapper<>(
                            "Envío eliminado exitosamente",
                            0,
                            null));
        } catch (ResourceNotFoundException e) {
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }
}