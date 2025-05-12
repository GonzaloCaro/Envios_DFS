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
//

import com.envios.envios.hateoas.EnvioModelAssembler;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final EnvioService envioService;
    private final EnvioMapper envioMapper;
    private final EnvioModelAssembler envioModelAssembler;

    public EnvioController(EnvioService envioService, EnvioMapper envioMapper,
            EnvioModelAssembler envioModelAssembler) {
        this.envioService = envioService;
        this.envioMapper = envioMapper;
        this.envioModelAssembler = envioModelAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<EnvioDTO>>> getAllEnvios() {
        log.debug("Controller: Obteniendo todos los envíos");
        List<Envio> envios = envioService.getAllEnvios();
        if (envios.isEmpty()) {
            log.error("Controller: No se encontraron envíos");
            throw new ResourceNotFoundException("No se encontraron envíos");
        }
        log.debug("Controller: Se encontraron {} envíos", envios.size());

        List<EntityModel<EnvioDTO>> envioModels = envios.stream()
                .map(envioMapper::toDto)
                .map(envioModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(envioModels,
                        linkTo(methodOn(EnvioController.class).getAllEnvios()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnvioDTO>> getEnvioById(@PathVariable Long id) {
        log.debug("Controller: Obteniendo envío con id: {}", id);
        if (id == null) {
            log.error("Controller: ID de envío no puede ser nulo");
            throw new IllegalArgumentException("ID de envío no puede ser nulo");
        }
        try {
            Envio envio = envioService.getEnvioById(id);
            return ResponseEntity.ok(
                    envioModelAssembler.toModel(envioMapper.toDto(envio)));
        } catch (ResourceNotFoundException e) {
            log.error("Controller: Envío no encontrado con id: {}", id);
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @PostMapping
    public ResponseEntity<EntityModel<EnvioDTO>> createEnvio(@Valid @RequestBody EnvioDTO envioDTO) {
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
                    .body(envioModelAssembler.toModel(envioMapper.toDto(createdEnvio)));
        } catch (RuntimeException e) {
            log.error("Controller: Error al crear el envío: {}", e.getMessage());
            throw e; // Será manejado por GlobalExceptionHandler
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<EnvioDTO>> updateEnvio(@Valid @PathVariable Long id,
            @RequestBody EnvioDTO envioDTO) {
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
            return ResponseEntity.ok(envioModelAssembler.toModel(responseDto));
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