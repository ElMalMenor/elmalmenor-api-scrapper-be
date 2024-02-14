package org.elmalmenor.api.infra.database.service;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.database.model.Politician;
import org.elmalmenor.api.infra.database.model.Profession;
import org.elmalmenor.api.infra.database.repository.ProfessionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elmalmenor.api.utils.Utils.toListWithDivider;
import static org.elmalmenor.api.utils.Utils.toTitleCase;

@Service
@RequiredArgsConstructor
public class RelationProfessionService {

    private final ProfessionRepository professionRepository;

    private final Set<Profession> cacheProfession = new HashSet<>();

    public synchronized void construct(Politician politician, PoliticoModel politicoModel) {

        String profession = toTitleCase(politicoModel.getProfesion());

        List<String> toNormalize = toListWithDivider(profession);

        Set<Profession> professions = toNormalize.stream()
                .map(this::getProfessionOrSave)
                .collect(Collectors.toSet());

        politician.setProfessions(professions);
    }

    private synchronized Profession getProfessionOrSave(String name) {
        return cacheProfession.stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    Optional<Profession> optional = professionRepository.findByName(name);

                    if (optional.isPresent()) {
                        cacheProfession.add(optional.get());
                        return optional.get();
                    }

                    Profession p = new Profession();
                    p.setName(name);
                    professionRepository.saveAndFlush(p);
                    cacheProfession.add(p);
                    return p;
                });
    }

}
