package Generador;

import Informacion.GPSData;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class GPSDataGenerator {

    private static final String[] BUS_IDS = {"BUS01", "BUS02", "BUS03"};

    // Metodo para generar de manera aleatoria la longitud y la latitud
    public static ArrayList<GPSData> generateSimulatedData(int minutes) {
        ArrayList<GPSData> dataList = new ArrayList<>();
        Random rand = new Random();
        LocalDateTime startTime = LocalDateTime.of(2025, 3, 25, 8, 0);

        // For each para ir agregando de manera random decimales a la longitud y la latitud
        for (String busId : BUS_IDS) {
            double lat = 40.417 + rand.nextDouble() * 0.01;
            double lon = -3.704 + rand.nextDouble() * 0.01;

            // Dentro del for each tomando en cuenta el parametro de la latitud y la longitud cambiadas voy minuto por minuto agregando informacion
            for (int i = 0; i < minutes; i++) {
                double speed = rand.nextDouble() < 0.1 ? 0 : rand.nextDouble() * 50;


                dataList.add(new GPSData(
                        busId,
                        startTime.plusMinutes(i),
                        lat + (rand.nextDouble() - 0.5) / 100,
                        lon + (rand.nextDouble() - 0.5) / 100,
                        speed
                ));
            }
        }
        return dataList;
    }

    // Metodo para guardar toda la informacion de los autobuses en un CSV
    public static void guardarCSV(ArrayList<GPSData> dataList, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {

            writer.write(GPSData.csvHeader() + "\n");
            // For each para poder escribir toda la informacion de los autobuses en el archivo CSV previamente creado
            for (GPSData data : dataList) {
                writer.write(data.toCSV() + "\n");
            }
        }
    }

}
