package Exportador;

import Informacion.GPSData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GPSDataExporter {

    public static void exportarUltimaPosicionJSON(ArrayList<GPSData> datos) {
        // Guardar la última posición por bus
        Map<String, GPSData> ultimaPosicionPorBus = new HashMap<>();

        for (GPSData data : datos) {
            String busId = data.getBusId();
            if (!ultimaPosicionPorBus.containsKey(busId) ||
                    data.getTimestamp().isAfter(ultimaPosicionPorBus.get(busId).getTimestamp())) {
                ultimaPosicionPorBus.put(busId, data);
            }
        }

        // Crear archivo JSON para cada bus
        for (String busId : ultimaPosicionPorBus.keySet()) {
            GPSData lastData = ultimaPosicionPorBus.get(busId);

            String json =
                    "  \"busId\": \"" + lastData.getBusId() + "\",\n" +
                    "  \"latitude\": " + String.format("%.6f", lastData.getLatitude()) + ",\n" +
                    "  \"longitude\": " + String.format("%.6f", lastData.getLongitude()) + ",\n" +
                    "  \"timestamp\": \"" + lastData.getTimestamp().toString() + "\"\n" +
                    "}";

            String filename = busId.toLowerCase() + "_status.json";

            // Escritura del archivo JSON
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(json);
                System.out.println("Archivo JSON generado: " + filename);
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo " + filename + ": " + e.getMessage());
            }
        }
    }
}
