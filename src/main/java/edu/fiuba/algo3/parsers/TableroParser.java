package edu.fiuba.algo3.parsers;

import edu.fiuba.algo3.modelo.celdas.Celda;
import edu.fiuba.algo3.modelo.Tablero;
import edu.fiuba.algo3.modelo.excepcion.ArchivoNoEncontradoError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TableroParser {

    public Tablero leerArchivo(String ruta) throws ArchivoNoEncontradoError, IOException, ParseException {

        try (FileReader lectorJson = new FileReader(System.getProperty("user.dir") + ruta)) {
            JSONParser jsonParser = new JSONParser();
            JSONObject json = (JSONObject) jsonParser.parse(lectorJson);
            return generarTablero(json);
        } catch (FileNotFoundException e) {
            throw new ArchivoNoEncontradoError(System.getProperty("user.dir") + ruta);
        }
    }

    private Tablero generarTablero(JSONObject jsonTablero) {
        ArrayList<Celda> celdas = this.parsearCeldas(jsonTablero);
        ArrayList<Integer> dimensiones = this.parsearDimensiones(jsonTablero);
        int ancho = dimensiones.get(0);
        int largo = dimensiones.get(1);
        Tablero tablero = new Tablero(ancho, largo);
        tablero.armarMapa(celdas);
        return tablero;
    }

    private ArrayList<Integer> parsearDimensiones(JSONObject tablero) {
        JSONObject mapaJson = (JSONObject) tablero.get("mapa");

        ArrayList<Integer> dimensiones = new ArrayList<>();
        dimensiones.add((int) ((long) mapaJson.get("ancho")));
        dimensiones.add((int) ((long) mapaJson.get("largo")));

        return dimensiones;
    }

    private ArrayList<Celda> parsearCeldas(JSONObject tablero) throws ArchivoNoEncontradoError {
        JSONArray celdas = (JSONArray)((JSONObject) tablero.get("camino")).get("celdas");

        if (celdas == null) { throw new ArchivoNoEncontradoError("El mapa no es valido"); }
        CeldaParser celdaParser = new CeldaParser();
        ArrayList<Celda> celdasParseadas = new ArrayList<>();

        for (Object celda : celdas) {
            Celda celdaCreada = celdaParser.parse((JSONObject) celda);
            celdasParseadas.add(celdaCreada);
        }

        return celdasParseadas;
    }
}
