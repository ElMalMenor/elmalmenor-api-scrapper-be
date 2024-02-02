package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.PublicFunction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicFunctionRepository extends JpaRepository<PublicFunction, Integer> {

    Optional<PublicFunction> findByName(String name);

}
