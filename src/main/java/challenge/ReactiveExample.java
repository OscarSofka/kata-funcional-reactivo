package challenge;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ReactiveExample {

    public static final int VALOR_PERMITIDO = 15;
    public static final double PUNTAJE_MINIMO = 75;
    private Flux<Estudiante> estudianteList;

    public ReactiveExample() {
        //TODO: convertir los estudiantes a un Flux

        estudianteList = Flux.fromIterable(
                List.of(
                        new Estudiante("raul", 30, List.of(1, 2, 1, 4, 5)),
                        new Estudiante("andres", 35, List.of(4, 2, 4, 3, 5)),
                        new Estudiante("juan", 75, List.of(3, 2, 4, 5, 5)),
                        new Estudiante("pedro", 80, List.of(5, 5, 4, 5, 5)),
                        new Estudiante("santiago", 40, List.of(4, 5, 4, 5, 5))
                )
        );
    }

    //TODO: suma de puntajes
    public Mono<Integer> sumaDePuntajes() {
        return estudianteList.map(mapeoDeEstudianteAPuntaje())
                .reduce(0, Integer::sum);
    }

    private Function<Estudiante, Integer> mapeoDeEstudianteAPuntaje() {
        return Estudiante::getPuntaje;
    }

    //TODO: mayor puntaje de estudiante
    public Flux<Estudiante> mayorPuntajeDeEstudiante(int limit) {
        return estudianteList.sort(Comparator.reverseOrder())
                .take(limit);
    }

    //TODO: total de asistencias de estudiantes con mayor puntaje basado en un  valor
    public Mono<Integer> totalDeAsistenciasDeEstudiantesConMayorPuntajeDe(int valor) {
        return estudianteList.filter(estudiante -> estudiante.getPuntaje() > valor)
                .flatMapIterable(Estudiante::getAsistencias)
                .reduce(Integer::sum);
    }

    //TODO: el estudiante tiene asistencias correctas
    public Mono<Boolean> elEstudianteTieneAsistenciasCorrectas(Estudiante estudiante) {
        return Mono.just(estudiante)
                .filter(asistenciasPemitidas())
                .hasElement();
    }

    private Predicate<Estudiante> asistenciasPemitidas() {
        return estudiante -> estudiante.getAsistencias()
                .stream()
                .reduce(0, Integer::sum) >= VALOR_PERMITIDO;
    }

    //TODO: promedio de puntajes por estudiantes
    public Mono<Double> promedioDePuntajesPorEstudiantes() {
        return estudianteList.collect(
                Collectors.averagingInt(Estudiante::getPuntaje)
        );
    }


    //TODO: los nombres de estudiante con puntaje mayor a un valor
    public Flux<String> losNombresDeEstudianteConPuntajeMayorA(int valor) {
        return estudianteList
                .filter(estudiante -> estudiante.getPuntaje() > valor)
                .map(Estudiante::getNombre);
    }

    //TODO: estudiantes aprovados
    public Flux<String> estudiantesAprobados() {
        return estudianteList.filter(estudiante -> estudiante.getPuntaje() >= PUNTAJE_MINIMO)
                .doOnNext(estudiante -> estudiante.setAprobado(Boolean.TRUE))
                .filter(Estudiante::isAprobado)
                .map(Estudiante::getNombre);
    }


}