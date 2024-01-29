package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DistritoRepository extends JpaRepository<Distrito, Integer> {

    Optional<Distrito> findDistritoByDescripcion(String descripcion);

}
