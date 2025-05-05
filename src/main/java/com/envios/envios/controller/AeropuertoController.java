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

// 
import com.envios.envios.hateoas.AeropuertoModelAssembler;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Slf4j
@RestController
@RequestMapping("/api/aeropuertos")
public class AeropuertoController {

    private final AeropuertoService aeropuertoService;
    private final AeropuertoModelAssembler aeropuertoModelAssembler;

    public AeropuertoController(AeropuertoService aeropuertoService,
            AeropuertoModelAssembler aeropuertoModelAssembler) {
        this.aeropuertoService = aeropuertoService;
        this.aeropuertoModelAssembler = aeropuertoModelAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Aeropuerto>>> getAllAeropuertos() {

        log.debug("Controller: Obteniendo todos los aeropuertos");

        List<Aeropuerto> aeropuertos = aeropuertoService.getAllAeropuertos();

        if (aeropuertos.isEmpty()) {
            log.error("Controller: No se encontraron aeropuertos");
            throw new ResourceNotFoundException("No se encontraron aeropuertos");
        }

        log.debug("Controller: Se encontraron {} aeropuertos", aeropuertos.size());

        List<EntityModel<Aeropuerto>> aeropuertoModels = aeropuertos.stream()
                .map(aeropuertoModelAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(aeropuertoModels,
                        linkTo(methodOn(AeropuertoController.class).getAllAeropuertos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Aeropuerto>> getAeropuertoById(@PathVariable Long id) {

        log.debug("Controller: Obteniendo aeropuerto con id: {}", id);

        if (id == null) {
            log.error("Controller: ID de aeropuerto no puede ser nulo");
            throw new IllegalArgumentException("ID de aeropuerto no puede ser nulo");
        }

        try {
            Aeropuerto aeropuerto = aeropuertoService.getAeropuertoById(id);

            return ResponseEntity.ok(aeropuertoModelAssembler.toModel(aeropuerto));

        } catch (ResourceNotFoundException e) {

            log.error("Controller: Aeropuerto no encontrado con id: {}", id);

            throw e;
        }
    }

    // Crear un nuevo aeropuerto
    @PostMapping
    public ResponseEntity<EntityModel<Aeropuerto>> createAeropuerto(@Valid @RequestBody Aeropuerto aeropuerto) {
        log.debug("Controller: Creando nuevo aeropuerto: {}", aeropuerto.getNombre());

        try {
            Aeropuerto createdAeropuerto = aeropuertoService.createAeropuerto(aeropuerto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(aeropuertoModelAssembler.toModel(createdAeropuerto));
        } catch (IllegalArgumentException e) {
            log.error("Controller: Error al crear aeropuerto: {}", e.getMessage());
            throw e;
        }
    }

    // Actualizar un aeropuerto existente
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Aeropuerto>> updateAeropuerto(@PathVariable Long id,
            @RequestBody Aeropuerto aeropuertoDetails) {

        log.debug("Controller: Actualizando aeropuerto con id: {}", id);
        if (id == null) {

            log.error("Controller: ID de aeropuerto no puede ser nulo");

            throw new IllegalArgumentException("ID de aeropuerto no puede ser nulo");
        }
        try {

            Aeropuerto updatedAeropuerto = aeropuertoService.updateAeropuerto(id, aeropuertoDetails);

            return ResponseEntity.ok(aeropuertoModelAssembler.toModel(updatedAeropuerto));

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