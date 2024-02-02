package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District, Integer> {

    Optional<District> findByName(String name);

}
