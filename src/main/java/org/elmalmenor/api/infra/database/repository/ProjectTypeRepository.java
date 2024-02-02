package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectTypeRepository extends JpaRepository<ProjectType, Integer> {

    Optional<ProjectType> findByName(String name);

}
