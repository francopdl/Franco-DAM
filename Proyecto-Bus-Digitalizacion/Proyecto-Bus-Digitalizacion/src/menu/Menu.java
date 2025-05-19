package menu;

import Analisis.GPSAnalyzer;
import Exportador.GPSDataExporter;
import Informacion.GPSData;
import java.util.*;
import java.io.IOException;
import java.util.Map;

public class Menu {
    private final ArrayList<GPSData> datos;
    private final Scanner scanner;

    public Menu(ArrayList<GPSData> datos, Scanner scanner) {
        this.datos = datos;
        this.scanner = scanner;
    }

    public void mostrar() {
        boolean salir = false;

        while (!salir) {
            System.out.println("===== MENÚ =====");
            System.out.println("1. Ver coordenadas por autobús");
            System.out.println("2. Análisis de datos (velocidad media y paradas)");
            System.out.println("3. Exportar última posición de cada bus a JSON");
            System.out.println("4. Mostrar estado actual de un autobús");
            System.out.println("5. Modificar recorrido de un autobús");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1" -> mostrarCoordenadasPorBus();
                case "2" -> realizarAnalisis();
                case "3" -> {
                    GPSDataExporter.exportarUltimaPosicionJSON(datos);
                    System.out.println();
                }
                case "4" -> mostrarEstadoActualPorBus();
                case "5" -> modificarRecorrido();
                case "6" -> {
                    salir = true;
                    System.out.println("Saliendo del programa...");
                }
                default -> System.out.println("Opción inválida.\n");
            }
        }
    }

    private void mostrarCoordenadasPorBus() {
        System.out.println("Seleccione un autobús:");
        System.out.println("1. BUS01");
        System.out.println("2. BUS02");
        System.out.println("3. BUS03");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine().trim();

        String busId = switch (opcion) {
            case "1" -> "BUS01";
            case "2" -> "BUS02";
            case "3" -> "BUS03";
            default -> {
                System.out.println("Opción inválida.\n");
                yield null;
            }
        };

        if (busId != null) {
            System.out.println("\nCoordenadas de " + busId + ":");
            for (GPSData data : datos) {
                if (data.getBusId().equals(busId)) {
                    System.out.printf("Hora: %s | Lat: %.6f | Lon: %.6f | Velocidad: %.2f km/h\n",
                            data.getTimestamp(),
                            data.getLatitude(),
                            data.getLongitude(),
                            data.getSpeed());
                }
            }
            System.out.println();
        }
    }

    private void realizarAnalisis() {
        System.out.println("\n=== Análisis de Datos ===");

        Map<String, Double> velocidades = GPSAnalyzer.calcularVelocidadMedia(datos);
        Map<String, Integer> paradas = GPSAnalyzer.contarParadas(datos);

        System.out.println("\nVelocidad media por autobús:");
        for (String busId : velocidades.keySet()) {
            System.out.printf("%s: %.2f km/h\n", busId, velocidades.get(busId));
        }

        System.out.println("\nNúmero de paradas por autobús:");
        for (String busId : paradas.keySet()) {
            System.out.printf("%s: %d paradas\n", busId, paradas.get(busId));
        }

        System.out.println();
    }

    private void mostrarEstadoActualPorBus() {
        System.out.println("Seleccione un autobús para ver su estado actual:");
        System.out.println("1. BUS01");
        System.out.println("2. BUS02");
        System.out.println("3. BUS03");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine().trim();

        String busId = switch (opcion) {
            case "1" -> "BUS01";
            case "2" -> "BUS02";
            case "3" -> "BUS03";
            default -> {
                System.out.println("Opción inválida.\n");
                yield null;
            }
        };

        if (busId != null) {
            GPSData ultimoDato = null;
            for (GPSData data : datos) {
                if (data.getBusId().equals(busId)) {
                    if (ultimoDato == null || data.getTimestamp().isAfter(ultimoDato.getTimestamp())) {
                        ultimoDato = data;
                    }
                }
            }

            if (ultimoDato != null) {
                System.out.printf("\nEstado actual de %s:\n", busId);
                System.out.printf("Hora: %s\n", ultimoDato.getTimestamp());
                System.out.printf("Latitud: %.6f\n", ultimoDato.getLatitude());
                System.out.printf("Longitud: %.6f\n", ultimoDato.getLongitude());
                System.out.printf("Velocidad: %.2f km/h\n\n", ultimoDato.getSpeed());
            } else {
                System.out.println("No se encontraron datos para el bus seleccionado.\n");
            }
        }
    }

    private void modificarRecorrido() {
        System.out.println("Seleccione un autobús para modificar su recorrido:");
        System.out.println("1. BUS01");
        System.out.println("2. BUS02");
        System.out.println("3. BUS03");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine().trim();

        String busId = switch (opcion) {
            case "1" -> "BUS01";
            case "2" -> "BUS02";
            case "3" -> "BUS03";
            default -> {
                System.out.println("Opción inválida.\n");
                yield null;
            }
        };

        if (busId == null) return;

        try {
            System.out.print("Ingrese minuto de inicio para modificar (0-59): ");
            int minutoInicio = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Ingrese minuto de fin para modificar (0-59): ");
            int minutoFin = Integer.parseInt(scanner.nextLine().trim());

            if (minutoInicio < 0 || minutoFin > 59 || minutoInicio > minutoFin) {
                System.out.println("Rango de minutos inválido.\n");
                return;
            }

            System.out.print("Ingrese nueva latitud base (ejemplo 40.420): ");
            double nuevaLatitud = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Ingrese nueva longitud base (ejemplo -3.700): ");
            double nuevaLongitud = Double.parseDouble(scanner.nextLine().trim());

            ArrayList<GPSData> datosNuevos = new ArrayList<>();

            for (GPSData d : datos) {
                if (d.getBusId().equals(busId)
                        && d.getTimestamp().getMinute() >= minutoInicio
                        && d.getTimestamp().getMinute() <= minutoFin) {
                    GPSData nuevo = new GPSData(
                            d.getBusId(),
                            d.getTimestamp(),
                            nuevaLatitud + (Math.random() - 0.5) / 100,
                            nuevaLongitud + (Math.random() - 0.5) / 100,
                            d.getSpeed()
                    );
                    datosNuevos.add(nuevo);
                } else {
                    datosNuevos.add(d);
                }
            }

            datos.clear();
            datos.addAll(datosNuevos);

            System.out.println("Recorrido modificado exitosamente para " + busId + " entre minutos "
                    + minutoInicio + " y " + minutoFin + ".\n");

        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida, por favor ingrese números válidos.\n");
        }
    }
}
