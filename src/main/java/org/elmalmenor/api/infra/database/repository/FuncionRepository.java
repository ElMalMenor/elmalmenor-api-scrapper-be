package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Funcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionRepository extends JpaRepository<Funcion, Integer> {

    Optional<Funcion> findFuncionByDescripcion(String descripcion);

}
