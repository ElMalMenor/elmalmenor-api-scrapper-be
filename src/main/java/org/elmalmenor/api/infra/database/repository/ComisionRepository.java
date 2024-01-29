package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Comision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComisionRepository extends JpaRepository<Comision, Integer> {

    Optional<Comision> findComisionByDescripcion(String descripcion);

}
