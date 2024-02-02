package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.CommissionPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommissionPositionRepository extends JpaRepository<CommissionPosition, Integer> {

    Optional<CommissionPosition> findByName(String name);

}
