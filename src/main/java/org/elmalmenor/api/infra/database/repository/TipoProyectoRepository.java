package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.TipoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoProyectoRepository extends JpaRepository<TipoProyecto, Integer> {

    Optional<TipoProyecto> findTipoProyectoByDescripcion(String descripcion);

}
