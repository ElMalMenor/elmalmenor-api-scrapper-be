package org.elmalmenor.api.infra.scraper;

import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.scraper.mapper.DiputadoMapper;
import org.elmalmenor.api.infra.scraper.mapper.ScrapperMapper;
import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.ContactoRaw;
import org.elmalmenor.api.infra.scraper.model.DiputadoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.elmalmenor.api.infra.scraper.spec.Scrabble;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlPage;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ScrapperService implements Scrabble {

    private final ScrapperMapper scrapperMapper;
    private final DiputadoMapper diputadoMapper;

    @Override
    public Stream<DiputadoModel> scrap() {
        return diputadoMapper.map(scrapDiputados());
    }

    private Stream<DiputadoRaw> scrapDiputados() {

//        List<DiputadosRaw> miniTest = Arrays.asList(diputadosRawsList.get(0),
//                diputadosRawsList.get(1),
//                diputadosRawsList.get(2));
//
//        miniTest.parallelStream().forEach(e -> {
//            e.setContactoRaw(scrapContactoRaw(e));
//            e.setComisionRawSet(scrapComisionRaw(e));
//        });
//
//        System.out.println(miniTest);


        return scrapDiputadosRawList().parallel().peek(e -> {
            e.setContactoRaw(scrapContactoRaw(e));
            e.setComisionRawSet(scrapComisionRaw(e));
            e.setProyectoRaws(scrapProyectosRaw(e));
        });

    }

    @Retryable(backoff = @Backoff(delay = 1000))
    private Stream<DiputadoRaw> scrapDiputadosRawList() {
        try (WebClient webClient = webClient()) {
            HtmlPage page = webClient.getPage("https://www.diputados.gov.ar/diputados/index.html");

            DomNodeList<DomNode> rows = page.querySelectorAll("#tablaDiputados tbody tr");

            return scrapperMapper.map(rows);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Retryable(backoff = @Backoff(delay = 1000))
    private ContactoRaw scrapContactoRaw(DiputadoRaw diputadoRaw) {

        try (WebClient webClient = webClient()) {

            if (diputadoRaw.getDetallePath().contains("//")) {
                return null;
            }

            HtmlPage page = webClient.getPage("https://www.diputados.gov.ar" + diputadoRaw.getDetallePath());

            DomNode node = page.querySelector(".box-datosPersonales");

            return scrapperMapper.mapContacto(node);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Retryable(backoff = @Backoff(delay = 1000))
    private Set<ComisionRaw> scrapComisionRaw(DiputadoRaw diputadoRaw) {

        try (WebClient webClient = webClient()) {

            if (diputadoRaw.getDetallePath().contains("//")) {
                return null;
            }

            HtmlPage page = webClient.getPage("https://www.diputados.gov.ar"
                    + diputadoRaw.getDetallePath() + "comisiones.html");

            DomNodeList<DomNode> rows = page.querySelectorAll("#tablaComisiones tbody tr");

            return scrapperMapper.mapComisiones(rows);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Retryable(backoff = @Backoff(delay = 1000))
    private Set<ProyectoRaw> scrapProyectosRaw(DiputadoRaw diputadoRaw) {

        try (WebClient webClient = webClient()) {

            if (diputadoRaw.getDetallePath().contains("//")) {
                return null;
            }

            Set<ProyectoRaw> proyectos = new HashSet<>();

            HtmlPage page = webClient.getPage("https://www.diputados.gov.ar"
                    + diputadoRaw.getDetallePath() + "listado-proyectos.html");

            DomNodeList<DomNode> rows;

            DomNodeList<DomNode> paginator = page.querySelectorAll(".page-link");

            if (paginator.isEmpty()) {
                return null;
            }

            int lastPageNum = Integer.parseInt(paginator.getLast().getAttributes().getNamedItem("href")
                    .getNodeValue().split("=")[1]);

            for (int i = 1; i <= lastPageNum; i++) {

                page = webClient.getPage("https://www.diputados.gov.ar"
                        + diputadoRaw.getDetallePath() + "listado-proyectos.html?pagina="+i);

                rows = page.querySelectorAll("#tablesorter tbody tr");

                proyectos.addAll(scrapperMapper.mapProyectos(rows));

            }

            return proyectos;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private WebClient webClient() {
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        return client;
    }

}
