package edu.fiuba.algo3.entregas;


import com.tngtech.archunit.thirdparty.com.google.common.collect.Table;
import edu.fiuba.algo3.modelo.excepcion.PasaronTreintaRondasYnoHuboGanadorError;
import javafx.scene.control.Tab;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import edu.fiuba.algo3.modelo.*;
import edu.fiuba.algo3.modelo.afectantes.*;
import edu.fiuba.algo3.modelo.celdas.*;
import edu.fiuba.algo3.modelo.excepcion.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class CasosDeUsoSemana1Test {

    public void inhabilitarGladiador(Gladiador gladiador, int i) {
        Fiera fiera = new Fiera();

        for (int j = 0; j < i; j++) {
            fiera.afectar(gladiador);
        }
    }

    public void potenciarHasta(Gladiador gladiador, int cantidad) {
        Afectante mejora = new Potenciador();

        for (int i = 0; i < cantidad; i++) {
            mejora.afectar(gladiador);
        }
    }

    public void ascenderASemiSenior(Jugador jugador, Dado dado, Tablero tablero) {
        for (int i = 0; i < 8; i++) {
            jugador.jugarTurno(dado, tablero);
        }
    }

    @Test
    public void test01SeInicializaUnJugadorConLaEnergiaYElEquipamientoCorrecto() {
        Logger logger = LogManager.getLogger();
        int primerTurno = 1;

        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        Dado dado = new Dado(6);

        celdas.add(celdaInicial);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        inhabilitarGladiador(gladiador, 1);
        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, primerTurno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test02JugadorSaleCorrectamenteDeLaCasillaInicial() {
        Logger logger = LogManager.getLogger();
        int primerTurno = 1;
        Dado dado = new Dado(1);

        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaComun celdaComun = new CeldaComun(0, 1, new Vacio(), new Fiera(), logger);

        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaComun);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, primerTurno);

        Assertions.assertSame(celdaProxima, celdaActual);
    }

    @Test
    public void test03UnJugadorSinEnergiaPierdeElTurno() {
        Logger logger = LogManager.getLogger();
        int primerTurno = 1;

        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        Dado dado = new Dado(6);

        celdas.add(celdaInicial);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        inhabilitarGladiador(gladiador, 1); //pierde su energia
        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, primerTurno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test04AlRecibirComidaSuEnergiaIncrementaEnQuince() {
        Logger logger = LogManager.getLogger();
        int turno = 5;

        Dado dado = new Dado(6);
        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());
        Afectante comida = new Comida();

        comida.afectar(gladiador); //energia = 35
        inhabilitarGladiador(gladiador, 2);
        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test05AlRecibirUnPremioPorPrimeraVezRecibeUnCasco() {
        Logger logger = LogManager.getLogger();
        int turno = 4; //no va al caso del test, es lo mismo

        Dado dado = new Dado(6);
        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        potenciarHasta(gladiador, 1);  // El jugador obtiene un casco
        inhabilitarGladiador(gladiador, 2); // al ser atacado 2 veces por una fiera se queda sin energia.

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test06AlRecibirUnPremioPorTerceraVezObtieneEscudoYEspada() {
        Logger logger = LogManager.getLogger();
        int turno = 20;

        Dado dado = new Dado(6);
        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        potenciarHasta(gladiador, 3);  // El jugador obtiene un Escudo Y Espada.

        inhabilitarGladiador(gladiador, 10); //para sacarle toda la energia debe ser atacado 10 veces

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test07AlHaberUnCombateConFieraSiTieneCascoPierdeQuincePuntosDeEnergia() {
        Logger logger = LogManager.getLogger();
        int turno = 10;

        Dado dado = new Dado(1);
        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaComun celdaComun = new CeldaComun(0,1, new Vacio(), new Vacio(), logger);
        CeldaFinal celdaFinal = new CeldaFinal(0, 2, logger);

        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaComun);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,2);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());
        Afectante mejora = new Potenciador();
        Fiera fiera = new Fiera();

        mejora.afectar(gladiador);  // El jugador obtiene un casco.
        fiera.afectar(gladiador); //lo ataca la fiera y queda con energia = 5

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertSame(celdaProxima, celdaActual);

        fiera.afectar(gladiador);
        celdaProxima = tablero.avanzar(dado.tirar(), celdaActual);
        celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test08AlPasarOchoTurnosElGladiadorPasaDeNovatoASemiSenior() {
        Logger logger = LogManager.getLogger();
        Dado dado = new Dado(1);
        int turno = 8;

        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaFinal);
        
        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);

        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());
        Jugador jugador = new Jugador("Juan", gladiador, tablero.getCeldaInicial(),logger);

        ascenderASemiSenior(jugador, dado, tablero); //pasa a tener 25 de energia.
        inhabilitarGladiador(gladiador, 2); //atacado 2 veces por fiera

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertNotSame(celdaProxima, celdaActual);
    }

   @Test
    public void test09AlLlegarAlaMetaSinLaLlaveRetrocedeAlaMitadDeLasCasillas() {
       Logger logger = LogManager.getLogger();
       int turno= 19;

       Dado dado = new Dado(1);
       CeldaInicial celdaInicial = new CeldaInicial(0,0, logger);
       CeldaFinal celdaFinal = new CeldaFinal(0,1,logger);
       ArrayList<Celda> celdas = new ArrayList<>();
       celdas.add(celdaInicial);
       celdas.add(celdaFinal);

       Tablero tablero = new Tablero(1,1);
       tablero.armarMapa(celdas);

       Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());
       Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial); //Celda Final
       Celda celdaActual = gladiador.mover(celdaProxima, turno); //es la celda inicial-> como no tiene la LLAVE vuelve al principio

       Assertions.assertNotSame(celdaProxima, celdaActual);
    }

    @Test
    public void test10AlSerAtacadoPorUnaFieraYConTodoElEquipamientoNoPierdeEnergia() {
        Logger logger = LogManager.getLogger();
        int turno = 10;

        Dado dado = new Dado(1);
        CeldaInicial celdaInicial = new CeldaInicial(0, 0, logger);
        CeldaComun celdaComun = new CeldaComun(0, 1, new Vacio(), new Vacio(), logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaComun);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);

        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        potenciarHasta(gladiador, 4);  /// El jugador obtiene una Llave.

        inhabilitarGladiador(gladiador, 1000); //no va a perder nunca energia al ser atacado infinitas veces

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertSame(celdaProxima, celdaActual);
    }

    @Test
    public void test11AlTenerLaLlaveYrecibirOtroPremioNoCambiaNada() {
        Logger logger = LogManager.getLogger();
        int turno = 10;

        Dado dado = new Dado(1);
        CeldaInicial celdaInicial = new CeldaInicial(0,0, logger);
        CeldaComun celdaComun = new CeldaComun(0,1, new Vacio(), new Vacio(), logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaComun);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        Gladiador gladiador = new Gladiador(logger, tablero.getCeldaInicial());

        potenciarHasta(gladiador, 5);  // El jugador obtiene una Llave y se le aplica una mejora mas (sin obtener nada)

        inhabilitarGladiador(gladiador, 1000); //no va a perder nunca energia al ser atacado infinitas veces

        Celda celdaProxima = tablero.avanzar(dado.tirar(), celdaInicial);
        Celda celdaActual = gladiador.mover(celdaProxima, turno);

        Assertions.assertSame(celdaProxima, celdaActual);
    }

    @Test
    public void test12AlPasarTreintaTurnosYnadieLlegaAlaMetaSeTerminoElJuego() {
        Logger logger =LogManager.getLogger();
        Juego juego = new Juego(logger);
        CeldaInicial celdaInicial = new CeldaInicial(0,0,logger);
        CeldaFinal celdaFinal = new CeldaFinal(0,1, logger);
        ArrayList<Celda> celdas = new ArrayList<>();
        celdas.add(celdaInicial);
        celdas.add(celdaFinal);

        Tablero tablero = new Tablero(1,1);
        tablero.armarMapa(celdas);
        ArrayList<String> nombresJugadores = new ArrayList<>();
        nombresJugadores.add("Pepe");
        nombresJugadores.add("juan");

        Assertions.assertFalse(juego.iniciarPartida(tablero, nombresJugadores));
    }
}
