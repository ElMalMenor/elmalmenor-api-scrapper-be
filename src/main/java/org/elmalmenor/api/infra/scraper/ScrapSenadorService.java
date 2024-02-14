package org.elmalmenor.api.infra.scraper;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.elmalmenor.api.domain.model.PoliticoModel;
import org.elmalmenor.api.infra.scraper.mapper.PoliticoRawMapper;
import org.elmalmenor.api.infra.scraper.mapper.ScrapperSenadorMapper;
import org.elmalmenor.api.infra.scraper.model.ComisionRaw;
import org.elmalmenor.api.infra.scraper.model.ContactoRaw;
import org.elmalmenor.api.infra.scraper.model.PoliticoRaw;
import org.elmalmenor.api.infra.scraper.model.ProyectoRaw;
import org.elmalmenor.api.infra.scraper.spec.Scrabble;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomNodeList;
import org.htmlunit.html.HtmlPage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.elmalmenor.api.utils.Utils.webClient;

@Service
@RequiredArgsConstructor
public class ScrapSenadorService implements Scrabble {

    private final String SENADO_BASE_URL = "https://www.senado.gob.ar";

    private final WebClient webClient = webClient();

    private final ScrapperSenadorMapper scrapperMapper;
    private final PoliticoRawMapper diputadoMapper;

    @Override
    public Stream<PoliticoModel> scrap() {
        return diputadoMapper.map(scrapSenadores());
    }

    private Stream<PoliticoRaw> scrapSenadores() {

        List<PoliticoRaw> politicos = new ArrayList<>();

        jsonSenadoresByUrl().forEach(e -> politicos.add(scrapperMapper.map((JSONObject) e)));

        return politicos.stream().parallel().peek(e -> {
            e.setContactoRaw(scrapContactoRaw(e));
            e.setComisionRawSet(scrapComisionRaw(e));
            e.setProyectoRaws(scrapProyectosRaw(e));
        });

    }

    private JSONArray jsonSenadoresByUrl() {

        try {
            String json = IOUtils.toString(URI.create(SENADO_BASE_URL + "/micrositios/DatosAbiertos/ExportarListadoSenadores/json"),
                    StandardCharsets.UTF_8);
            return new JSONObject(json).getJSONObject("table").getJSONArray("rows");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private ContactoRaw scrapContactoRaw(PoliticoRaw politicoRaw) {

        try {

            if (politicoRaw.getDetallePath().contains("//")) {
                return null;
            }

            HtmlPage page = webClient.getPage(SENADO_BASE_URL + politicoRaw.getDetallePath());

            DomNode node = page.querySelector(".nav2-list");

            return scrapperMapper.mapContacto(node);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Set<ComisionRaw> scrapComisionRaw(PoliticoRaw politicoRaw) {

        try {

            if (politicoRaw.getDetallePath().contains("//")) {
                return null;
            }

            HtmlPage page = webClient.getPage(SENADO_BASE_URL + politicoRaw.getDetallePath());

            DomNodeList<DomNode> rows = page.querySelectorAll("#Comision table tbody tr");

            return scrapperMapper.mapComisiones(rows);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Set<ProyectoRaw> scrapProyectosRaw(PoliticoRaw politicoRaw) {

        try {

            if (politicoRaw.getDetallePath().contains("//")) {
                return null;
            }

            Set<ProyectoRaw> proyectos = new HashSet<>();

            HtmlPage page = webClient.getPage(SENADO_BASE_URL + politicoRaw.getDetallePath());

            DomNodeList<DomNode> rows;

            DomNode paginator = page.querySelector("pagination");

            int lastPageNum;

            try {
                lastPageNum = Integer.parseInt(paginator.getAttributes().getNamedItem("href")
                        .getNodeValue().split("&ProyectosSenador=")[1]);
            } catch (Exception ex) {
                lastPageNum = 1;
            }

            for (int i = 1; i <= lastPageNum; i++) {

                page = webClient.getPage(SENADO_BASE_URL + politicoRaw.getDetallePath() + "?ProyectosSenador="+i);

                rows = page.querySelectorAll("#3 tbody tr");

                proyectos.addAll(scrapperMapper.mapProyectos(rows));

            }

            return proyectos;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
