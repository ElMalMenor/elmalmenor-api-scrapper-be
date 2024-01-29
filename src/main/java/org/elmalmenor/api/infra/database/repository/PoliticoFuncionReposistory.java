package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.PoliticoFuncion;
import org.elmalmenor.api.infra.database.model.embedded.PoliticoFuncionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliticoFuncionReposistory extends JpaRepository<PoliticoFuncion, PoliticoFuncionId> {
}
