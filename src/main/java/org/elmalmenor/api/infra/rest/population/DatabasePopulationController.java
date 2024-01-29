package org.elmalmenor.api.infra.rest.population;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.application.spec.DatabasePopulationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/database-population")
@RequiredArgsConstructor
public class DatabasePopulationController {

    private final DatabasePopulationProcessor databasePopulationProcessor;

    @PostMapping("/start")
    public void startScrapper() {

        databasePopulationProcessor.processPopulation();

    }

}
