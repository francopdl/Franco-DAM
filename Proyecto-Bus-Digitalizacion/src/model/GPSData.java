package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GPSData {
    private String busId;
    private LocalDateTime timestamp;
    private double latitude;
    private double longitude;
    private double speed;

    public GPSData(String busId, LocalDateTime timestamp, double latitude, double longitude, double speed) {
        this.busId = busId;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public String getBusId() { return busId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getSpeed() { return speed; }

    public String toCSV() {
        return String.format("%s,%s,%.6f,%.6f,%.2f",
                busId,
                timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                latitude, longitude, speed);
    }

    public static String csvHeader() {
        return "busId,timestamp,latitude,longitude,speed";
    }

    @Override
    public String toString() {
        return toCSV();
    }
}
