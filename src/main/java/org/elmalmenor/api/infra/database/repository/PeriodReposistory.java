package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodReposistory extends JpaRepository<Period, Integer> {
}
