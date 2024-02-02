package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionRepository extends JpaRepository<Profession, Integer> {

    Optional<Profession> findByName(String name);

}
