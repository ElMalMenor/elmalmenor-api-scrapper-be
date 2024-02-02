package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComisionRepository extends JpaRepository<Commission, Integer> {

    Optional<Commission> findByName(String name);

}
