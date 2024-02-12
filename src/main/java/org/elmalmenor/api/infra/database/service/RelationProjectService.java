package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.ProyectoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.Project;
import org.elmalmenor.api.infra.database.model.ProjectType;
import org.elmalmenor.api.infra.database.repository.ProjectRepository;
import org.elmalmenor.api.infra.database.repository.ProjectTypeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.elmalmenor.api.utils.Utils.toTitleCase;

@Service
@RequiredArgsConstructor
public class RelationProjectService {

    private final PoliticoMapper politicoMapper;
    private final ProjectRepository projectRepository;
    private final ProjectTypeRepository projectTypeRepository;

    private final Set<Project> cacheProject = new HashSet<>();
    private final Set<ProjectType> cacheProjectType = new HashSet<>();
    public synchronized void construct(ProyectoModel proyectoModel, Set<Project> projects) {
        if (proyectoModel.getExpediente().trim().length() > 4) {
            ProjectType projectType = getProjectTypeOrSave(politicoMapper.mapProjectType(proyectoModel));

            Project project = politicoMapper.mapProject(proyectoModel);
            project.setProjectType(projectType);

            projects.add(getProjectOrSave(project));
        }
    }

    private synchronized Project getProjectOrSave(Project project) {
        return cacheProject.stream()
                .filter(e -> e.getId().equals(project.getId()))
                .findFirst()
                .orElseGet(() -> {
                    projectRepository.saveAndFlush(project);
                    cacheProject.add(project);
                    return project;
                });
    }

    private synchronized ProjectType getProjectTypeOrSave(ProjectType projectType) {
        projectType.setName(toTitleCase(projectType.getName()));

        return cacheProjectType.stream()
                .filter(e -> e.getName().equals(projectType.getName()))
                .findFirst()
                .orElseGet(() -> {
                    projectTypeRepository.saveAndFlush(projectType);
                    cacheProjectType.add(projectType);
                    return projectType;
                });
    }

}
