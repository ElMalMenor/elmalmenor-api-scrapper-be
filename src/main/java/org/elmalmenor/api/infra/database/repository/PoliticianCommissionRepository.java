package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.PoliticianCommission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliticianCommissionRepository extends JpaRepository<PoliticianCommission, Integer> {
}
