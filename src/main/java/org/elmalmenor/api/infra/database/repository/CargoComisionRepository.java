package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.CargoComision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoComisionRepository extends JpaRepository<CargoComision, Integer> {

    Optional<CargoComision> findCargoComisionByDescripcion(String descripcion);

}
