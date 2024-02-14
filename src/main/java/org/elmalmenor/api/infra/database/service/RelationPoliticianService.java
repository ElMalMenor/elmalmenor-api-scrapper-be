package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.Politician;
import org.elmalmenor.api.infra.database.model.Project;
import org.elmalmenor.api.infra.database.repository.PoliticianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RelationPoliticianService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final PoliticoMapper politicoMapper;

    private final PoliticianRepository politicianRepository;
    private final RelationProfessionService relationProfessionService;
    private final RelationProjectService relationProjectService;
    private final RelationPeriodService relationPeriodService;

    public synchronized void construct(PoliticoModel politicoModel) {

        if (politicianRepository.findByFirstNameAndLastName(politicoModel.getNombre(), politicoModel.getApellido()).isEmpty()) {

            try {

                LOGGER.info("Procesando {}...: {} {}", politicoModel.getFuncion(),
                        politicoModel.getNombre(),
                        politicoModel.getApellido());

                Politician politico = politicoMapper.map(politicoModel);

                Set<Project> projects = new HashSet<>();

                if (Objects.nonNull(politicoModel.getProyectos())) {
                    politicoModel.getProyectos().forEach(p -> relationProjectService.construct(p, projects));
                    politico.setProjects(projects);
                }

                relationProfessionService.construct(politico, politicoModel);

                politicianRepository.saveAndFlush(politico);

                relationPeriodService.construct(politico, politicoModel);

            } catch (Exception ex) {
                LOGGER.error("Error: {} | {}", ex.getMessage(), politicoModel);
            }
        }

    }

}
