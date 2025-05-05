package com.envios.envios.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // Importa funciones para generar enlaces
import com.envios.envios.controller.AeropuertoController;
import com.envios.envios.model.Aeropuerto;

@Component
public class AeropuertoModelAssembler implements RepresentationModelAssembler<Aeropuerto, EntityModel<Aeropuerto>> {

    @Override
    public EntityModel<Aeropuerto> toModel(Aeropuerto aeropuerto) {
        return EntityModel.of(aeropuerto,
                linkTo(methodOn(AeropuertoController.class).getAeropuertoById(aeropuerto.getId())).withSelfRel(),
                linkTo(methodOn(AeropuertoController.class).getAllAeropuertos()).withRel("all"),
                linkTo(methodOn(AeropuertoController.class).createAeropuerto(aeropuerto)).withRel("create"),
                linkTo(methodOn(AeropuertoController.class).updateAeropuerto(aeropuerto.getId(), aeropuerto))
                        .withRel("update"),
                linkTo(methodOn(AeropuertoController.class).deleteAeropuerto(aeropuerto.getId())).withRel("delete"));
    }

}
