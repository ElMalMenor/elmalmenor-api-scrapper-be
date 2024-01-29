package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Bloque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloqueRepository extends JpaRepository<Bloque, Integer> {

    Optional<Bloque> findBloqueByDescripcion(String descripcion);

}
