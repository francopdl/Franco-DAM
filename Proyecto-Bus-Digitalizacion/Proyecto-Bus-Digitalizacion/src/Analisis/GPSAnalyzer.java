package Analisis;

import Informacion.GPSData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GPSAnalyzer {

    // Metodo para calcular la velocidad media de los autobuses
    public static Map<String, Double> calcularVelocidadMedia(ArrayList<GPSData> datos) {
        Map<String, Double> totalVelocidad = new HashMap<>();
        Map<String, Integer> conteo = new HashMap<>();

        for (GPSData data : datos) {
            String busId = data.getBusId();
            totalVelocidad.put(busId, totalVelocidad.getOrDefault(busId, 0.0) + data.getSpeed());
            conteo.put(busId, conteo.getOrDefault(busId, 0) + 1);
        }

        Map<String, Double> velocidadMedia = new HashMap<>();
        for (String busId : totalVelocidad.keySet()) {
            double promedio = totalVelocidad.get(busId) / conteo.get(busId);
            velocidadMedia.put(busId, promedio);
        }

        return velocidadMedia;
    }

    // Metodo para contar las paradas de cada autobus
    public static Map<String, Integer> contarParadas(ArrayList<GPSData> datos) /* Toma como parametro de entrada el arraylist de los datos de los autobuses */   {
        Map<String, Integer> paradas = new HashMap<>();

        // Recorro el for each
        for (GPSData data : datos) {
            // En caso de que la velociada sea igual a cero entra en el if
            if (data.getSpeed() == 0.0) {
                // Toma en cuenta cual es el autobus
                String busId = data.getBusId();
                // Contador de paradas, le suma 1 a la parada cuando la velocidad sea igual a 0
                paradas.put(busId, paradas.getOrDefault(busId, 0) + 1);
            }
        }
        // Devuelve la cantidad de paradas de los autobuses
        return paradas;
    }
}
