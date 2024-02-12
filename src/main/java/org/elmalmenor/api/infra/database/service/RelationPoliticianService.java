package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.DiputadoModel;
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

    public synchronized void construct(DiputadoModel diputado) {
        if (politicianRepository.findByFirstNameAndLastName(diputado.getNombre(), diputado.getApellido()).isEmpty()) {

            LOGGER.info("Procesando Diputado...: {} {}",diputado.getNombre(), diputado.getApellido());
            Politician politico = politicoMapper.map(diputado);

            Set<Project> projects = new HashSet<>();

            if (Objects.nonNull(diputado.getProyectos())) {
                diputado.getProyectos().forEach(p -> relationProjectService.construct(p, projects));
                politico.setProjects(projects);
            }

            relationProfessionService.construct(politico, diputado);

            politicianRepository.saveAndFlush(politico);

            relationPeriodService.construct(politico, diputado);

        }
    }

}
