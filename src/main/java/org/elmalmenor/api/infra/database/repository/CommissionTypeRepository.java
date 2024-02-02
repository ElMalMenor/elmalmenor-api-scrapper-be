package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.CommissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommissionTypeRepository extends JpaRepository<CommissionType, Integer> {

    Optional<CommissionType> findByName(String name);

}
