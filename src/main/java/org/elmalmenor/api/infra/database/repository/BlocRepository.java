package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlocRepository extends JpaRepository<Bloc, Integer> {

    Optional<Bloc> findByName(String name);

}
