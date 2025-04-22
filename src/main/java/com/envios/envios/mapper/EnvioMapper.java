package com.envios.envios.mapper;

import com.envios.envios.DTO.EnvioDTO;
import com.envios.envios.model.Aeropuerto;
import com.envios.envios.model.Envio;
import com.envios.envios.repository.AeropuertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnvioMapper {

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    public Envio toEntity(EnvioDTO dto) {
        Envio envio = new Envio();

        Aeropuerto origen = aeropuertoRepository.findById(dto.getOrigenId())
                .orElseThrow(() -> new RuntimeException("Aeropuerto origen no encontrado"));

        Aeropuerto destino = aeropuertoRepository.findById(dto.getDestinoId())
                .orElseThrow(() -> new RuntimeException("Aeropuerto destino no encontrado"));

        envio.setOrigen(origen);
        envio.setDestino(destino);
        envio.setDescripcion(dto.getDescripcion());

        return envio;
    }

    public EnvioDTO toDto(Envio envio) {
        EnvioDTO dto = new EnvioDTO();
        dto.setOrigenId(envio.getOrigen().getId());
        dto.setDestinoId(envio.getDestino().getId());
        dto.setDescripcion(envio.getDescripcion());
        return dto;
    }
}