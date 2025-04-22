package com.envios.envios.mapper;

import com.envios.envios.DTO.AeropuertoDTO;
import com.envios.envios.model.Aeropuerto;
import org.springframework.stereotype.Component;

@Component
public class AeropuertoMapper {

    public AeropuertoDTO toDto(Aeropuerto aeropuerto) {
        AeropuertoDTO dto = new AeropuertoDTO();
        dto.setId(aeropuerto.getId());
        dto.setNombre(aeropuerto.getNombre());
        dto.setPais(aeropuerto.getPais());
        dto.setCiudad(aeropuerto.getCiudad());
        dto.setDireccion(aeropuerto.getDireccion());
        return dto;
    }

    public Aeropuerto toEntity(AeropuertoDTO dto) {
        Aeropuerto aeropuerto = new Aeropuerto();
        aeropuerto.setId(dto.getId());
        aeropuerto.setNombre(dto.getNombre());
        aeropuerto.setPais(dto.getPais());
        aeropuerto.setCiudad(dto.getCiudad());
        aeropuerto.setDireccion(dto.getDireccion());
        return aeropuerto;
    }
}