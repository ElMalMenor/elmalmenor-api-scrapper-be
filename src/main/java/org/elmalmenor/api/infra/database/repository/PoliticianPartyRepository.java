package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.PoliticianParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliticianPartyRepository extends JpaRepository<PoliticianParty, Integer> {
}
