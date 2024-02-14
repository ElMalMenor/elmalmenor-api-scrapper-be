package org.elmalmenor.api.infra.scraper;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.scraper.mapper.PoliticoRawMapper;
import org.elmalmenor.api.infra.scraper.mapper.ScrapperDiputadoMapper;
import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.ContactoRaw;
import org.elmalmenor.api.infra.scraper.model.PoliticoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.elmalmenor.api.infra.scraper.spec.Scrabble;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.elmalmenor.api.utils.Utils.webClient;

@Service
@RequiredArgsConstructor
public class ScrapDiputadoService implements Scrabble {

    private final String DIPUTADOS_BASE_URL = "https://www.diputados.gov.ar";

    private final ScrapperDiputadoMapper scrapperMapper;
    private final PoliticoRawMapper diputadoMapper;

    @Override
    public Stream<PoliticoModel> scrap() {
        return diputadoMapper.map(scrapDiputados());
    }

    private Stream<PoliticoRaw> scrapDiputados() {

        return scrapDiputadosRawList().parallel().peek(e -> {
            e.setContactoRaw(scrapContactoRaw(e));
            e.setComisionRawSet(scrapComisionRaw(e));
            e.setProyectoRaws(scrapProyectosRaw(e));
        });

    }

    private Stream<PoliticoRaw> scrapDiputadosRawList() {
        try (WebClient webClient = webClient()) {
            HtmlPage page = webClient.getPage(DIPUTADOS_BASE_URL + "/diputados/index.html");

            DomNodeList<DomNode> rows = page.querySelectorAll("#tablaDiputados tbody tr");

            return scrapperMapper.map(rows);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ContactoRaw scrapContactoRaw(PoliticoRaw politicoRaw) {

        try (WebClient webClient = webClient()) {

            if (politicoRaw.getDetallePath().contains("//")) {
                return null;
            }

            HtmlPage page = webClient.getPage(DIPUTADOS_BASE_URL + politicoRaw.getDetallePath());

            DomNode node = page.querySelector(".box-datosPersonales");

            return scrapperMapper.mapContacto(node);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Set<ComisionRaw> scrapComisionRaw(PoliticoRaw politicoRaw) {

        try (WebClient webClient = webClient()) {

            if (politicoRaw.getDetallePath().contains("//")) {
                return null;
            }

            HtmlPage page = webClient.getPage(DIPUTADOS_BASE_URL
                    + politicoRaw.getDetallePath() + "comisiones.html");

            DomNodeList<DomNode> rows = page.querySelectorAll("#tablaComisiones tbody tr");

            return scrapperMapper.mapComisiones(rows);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Set<ProyectoRaw> scrapProyectosRaw(PoliticoRaw politicoRaw) {

        try (WebClient webClient = webClient()) {

            if (politicoRaw.getDetallePath().contains("//")) {
                return null;
            }

            Set<ProyectoRaw> proyectos = new HashSet<>();

            HtmlPage page = webClient.getPage(DIPUTADOS_BASE_URL
                    + politicoRaw.getDetallePath() + "listado-proyectos.html");

            DomNodeList<DomNode> rows;

            DomNodeList<DomNode> paginator = page.querySelectorAll(".page-link");

            if (paginator.isEmpty()) {
                return null;
            }

            int lastPageNum = Integer.parseInt(paginator.getLast().getAttributes().getNamedItem("href")
                    .getNodeValue().split("=")[1]);

            for (int i = 1; i <= lastPageNum; i++) {

                page = webClient.getPage(DIPUTADOS_BASE_URL
                        + politicoRaw.getDetallePath() + "listado-proyectos.html?pagina="+i);

                rows = page.querySelectorAll("#tablesorter tbody tr");

                proyectos.addAll(scrapperMapper.mapProyectos(rows));

            }

            return proyectos;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
