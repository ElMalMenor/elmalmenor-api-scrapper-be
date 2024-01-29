package org.elmalmenor.api.infra.database.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.elmalmenor.api.domain.model.DiputadoModel;
import org.elmalmenor.api.infra.database.mapper.PoliticoMapper;
import org.elmalmenor.api.infra.database.model.*;
import org.elmalmenor.api.infra.database.repository.*;
import org.elmalmenor.api.infra.database.spec.Populatable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopulationService implements Populatable {

    private final PoliticoMapper politicoMapper;

    private final ProyectoRepository proyectoRepository;
    private final TipoProyectoRepository tipoProyectoRepository;
    private final FuncionRepository funcionRepository;
    private final TipoComisionRepository tipoComisionRepository;
    private final DistritoRepository distritoRepository;
    private final ComisionRepository comisionRepository;
    private final CargoComisionRepository cargoComisionRepository;
    private final BloqueRepository bloqueRepository;
    private final PoliticoRepository politicoRepository;
    private final PoliticoFuncionReposistory politicoFuncionReposistory;
    private final PoliticoComisionRepository politicoComisionRepository;

    @Override
    public void populateTable(Stream<DiputadoModel> diputados) {

        diputados.forEach(e -> {

            if (politicoRepository.findPoliticoByNombreAndApellido(e.getNombre(), e.getApellido()).isEmpty()) {

                System.out.println("Procesando Diputado...: " + e.getApellido() + " | " + e);
                Politico politico = politicoMapper.map(e);

                constructDistritoRelation(politico, e);
                constructBloqueRelation(politico, e);
                constructProyectosRelation(politico, e);
                constructComisionesRelation(politico, e);

                politicoRepository.save(politico);
                proyectoRepository.flush();

                constructFuncionRelation(politico, e);
            } else {
                System.out.println("Ya Procesado Diputado...: " + e.getApellido());
            }


        });

    }

    private void constructComisionesRelation(Politico politico, DiputadoModel diputadoModel) {

        if (!Objects.nonNull(diputadoModel.getComisiones()))
            return;

        diputadoModel.getComisiones().forEach(e -> {

            CargoComision cargoComision = getCargoComisionOrSave(politicoMapper.mapCargoComision(e));
            Comision comision = getComisionOrSave(politicoMapper.mapComision(e));

            PoliticoComision politicoComision = new PoliticoComision();

            politicoComision.setCargoComision(cargoComision);
            politicoComision.setPolitico(politico);
            politicoComision.setComision(comision);

            politicoComisionRepository.save(politicoComision);

        });

    }

    private void constructProyectosRelation(Politico politico, DiputadoModel diputadoModel) {

        Set<Proyecto> proyectos = new HashSet<>();

        if (!Objects.nonNull(diputadoModel.getProyectos()))
            return;

        diputadoModel.getProyectos().forEach(p -> {
            TipoProyecto tipoProyecto = getTipoProyectoOrSave(politicoMapper.mapTipoProyecto(p));

            Proyecto proyecto = politicoMapper.mapProyecto(p);
            proyecto.setTipoProyecto(tipoProyecto);

            proyecto = getProyectoOrSave(proyecto);

            proyectos.add(proyecto);
        });

        politico.setProyectos(proyectos);

    }

    private void constructBloqueRelation(Politico politico, DiputadoModel diputadoModel) {
        Bloque bloque = getBloqueOrSave(politicoMapper.mapBloque(diputadoModel));
        politico.setBloque(bloque);
    }

    private void constructFuncionRelation(Politico politico, DiputadoModel diputadoModel) {
        Funcion funcion = getFuncionOrSave(politicoMapper.mapFuncion(diputadoModel));

        PoliticoFuncion politicoFuncion = new PoliticoFuncion(
                politico,
                funcion,
                diputadoModel.getMandatoInicio(),
                diputadoModel.getMandatoFin(),
                true
        );

        politicoFuncionReposistory.save(politicoFuncion);
    }

    private void constructDistritoRelation(Politico politico, DiputadoModel diputadoModel) {
        Distrito distrito = getDistritoOrSave(politicoMapper.mapDistrito(diputadoModel));
        politico.setDistrito(distrito);
    }

    private Proyecto getProyectoOrSave(Proyecto proyecto) {
        return proyectoRepository
                .findById(proyecto.getIdProyecto())
                .orElseGet(() -> {
                    proyectoRepository.save(proyecto);
                    proyectoRepository.flush();
                    return proyecto;
                });
    }

    private TipoProyecto getTipoProyectoOrSave(TipoProyecto tipoProyecto) {
        return tipoProyectoRepository
                .findTipoProyectoByDescripcion(tipoProyecto.getDescripcion())
                .orElseGet(() -> {
                    tipoProyectoRepository.save(tipoProyecto);
                    proyectoRepository.flush();
                    return tipoProyecto;
                });
    }

    private Funcion getFuncionOrSave(Funcion funcion) {
        return funcionRepository
                .findFuncionByDescripcion(funcion.getDescripcion())
                .orElseGet(() -> {
                    funcionRepository.save(funcion);
                    proyectoRepository.flush();
                    return funcion;
                });
    }

    private TipoComision getTipoComisionOrSave(TipoComision tipoComision) {
        return tipoComisionRepository
                .findTipoComisionByDescripcion(tipoComision.getDescripcion())
                .orElseGet(() -> {
                    tipoComisionRepository.save(tipoComision);
                    proyectoRepository.flush();
                    return tipoComision;
                });
    }

    private Distrito getDistritoOrSave(Distrito distrito) {
        return distritoRepository
                .findDistritoByDescripcion(distrito.getDescripcion())
                .orElseGet(() -> {
                    distritoRepository.save(distrito);
                    proyectoRepository.flush();
                    return distrito;
                });
    }

    private Comision getComisionOrSave(Comision comision) {
        return comisionRepository
                .findComisionByDescripcion(comision.getDescripcion())
                .orElseGet(() -> {
                    comisionRepository.save(comision);
                    proyectoRepository.flush();
                    return comision;
                });
    }

    private CargoComision getCargoComisionOrSave(CargoComision comision) {
        return cargoComisionRepository
                .findCargoComisionByDescripcion(comision.getDescripcion())
                .orElseGet(() -> {
                    cargoComisionRepository.save(comision);
                    proyectoRepository.flush();
                    return comision;
                });
    }

    private Bloque getBloqueOrSave(Bloque bloque) {
        return bloqueRepository
                .findBloqueByDescripcion(bloque.getDescripcion())
                .orElseGet(() -> {
                    bloqueRepository.save(bloque);
                    return bloque;
                });
    }
}
