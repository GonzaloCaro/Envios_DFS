package com.envios.envios.repository;

import com.envios.envios.model.Aeropuerto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Long> {
    boolean existsByNombre(String nombre);
    
    @Query("SELECT COUNT(e) > 0 FROM Envio e WHERE e.origen.id = :aeropuertoId OR e.destino.id = :aeropuertoId")
    boolean isAeropuertoUsedInEnvio(Long aeropuertoId);
    
    Optional<Aeropuerto> findByNombre(String nombre);
}