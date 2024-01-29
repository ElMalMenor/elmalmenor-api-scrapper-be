package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.TipoComision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoComisionRepository extends JpaRepository<TipoComision, Integer> {

    Optional<TipoComision> findTipoComisionByDescripcion(String descripcion);

}
