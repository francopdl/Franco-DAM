package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GPSData {
    private String busId;
    private LocalDateTime timestamp;
    private double latitud;
    private double longitud;
    private double velocidad;

    public GPSData(String busId, LocalDateTime timestamp, double latitude, double longitude, double speed) {
        this.busId = busId;
        this.timestamp = timestamp;
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
    }

    public String getBusId() { return busId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getLatitude() { return latitud; }
    public double getLongitude() { return longitud; }
    public double getSpeed() { return velocidad; }

    public String toCSV() {
        return String.format("%s,%s,%.6f,%.6f,%.2f",
                busId,
                timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                latitud, longitud, velocidad);
    }

    public static String csvHeader() {
        return "busId,timestamp,latitude,longitude,speed";
    }

    @Override
    public String toString() {
        return toCSV();
    }
}
