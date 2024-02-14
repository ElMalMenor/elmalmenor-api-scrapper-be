package org.elmalmenor.api.application.service.population;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.application.spec.population.ScrapperProcessor;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.scraper.ScrapDiputadoService;
import org.elmalmenor.api.infra.scraper.ScrapSenadorService;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ScrapperExecutor implements ScrapperProcessor {

    private final ScrapDiputadoService scrabbleDiputados;
    private final ScrapSenadorService scrabbleSenadores;

    @Override
    public Stream<PoliticoModel> execScrapperDiputados() {
        return scrabbleDiputados.scrap();
    }

    @Override
    public Stream<PoliticoModel> execScrapperSenadores() {
        return scrabbleSenadores.scrap();
    }
}
