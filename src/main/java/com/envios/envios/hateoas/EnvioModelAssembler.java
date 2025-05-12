package com.envios.envios.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*; // Importa funciones para generar enlaces
import com.envios.envios.controller.EnvioController;
import com.envios.envios.DTO.EnvioDTO;

@Component
public class EnvioModelAssembler implements RepresentationModelAssembler<EnvioDTO, EntityModel<EnvioDTO>> {

    @Override
    public EntityModel<EnvioDTO> toModel(EnvioDTO envio) {
        return EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).getEnvioById(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).getAllEnvios()).withRel("all"),
                linkTo(methodOn(EnvioController.class).createEnvio(envio)).withRel("create"),
                linkTo(methodOn(EnvioController.class).updateEnvio(envio.getId(), envio)).withRel("update"),
                linkTo(methodOn(EnvioController.class).deleteEnvio(envio.getId())).withRel("delete"));
    }
    
}
