import Generador.GPSDataGenerator;
import Informacion.GPSData;
import menu.Menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<GPSData> datos = GPSDataGenerator.generateSimulatedData(60);

        try {
            GPSDataGenerator.guardarCSV(datos, "gps_data.csv");
            System.out.println("Datos generados y guardados en gps_data.csv\n");
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(datos, scanner);
        menu.mostrar();
        scanner.close();
    }
}
