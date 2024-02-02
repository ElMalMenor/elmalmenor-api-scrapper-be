package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Politician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoliticianRepository extends JpaRepository<Politician, Integer> {

    Optional<Politician> findByFirstNameAndLastName(String firstName, String lastName);

}
