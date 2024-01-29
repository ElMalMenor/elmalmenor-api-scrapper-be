package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Politico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoliticoRepository extends JpaRepository<Politico, Integer> {

    Optional<Politico> findPoliticoByNombreAndApellido(String nombre, String apellido);

}
