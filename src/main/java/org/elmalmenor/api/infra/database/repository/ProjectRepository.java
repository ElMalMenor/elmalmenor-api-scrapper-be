package org.elmalmenor.api.infra.database.repository;

import org.elmalmenor.api.infra.database.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {

}
