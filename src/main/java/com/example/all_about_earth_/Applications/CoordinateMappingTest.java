package com.example.all_about_earth_.Applications;

import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;

public class CoordinateMappingTest {
    public Sphere sphere; // Assume che sia il tuo oggetto sfera

    public void runCoordinateTests() {
        // Array di coordinate di test con descrizione
        TestLocation[] testLocations = {
                new TestLocation("Equatore (Greenwich)", 0, 0),
                new TestLocation("New York", 40.7, -74.0),
                new TestLocation("Tokyo", 35.7, 139.7),
                new TestLocation("Sydney", -33.9, 151.2),
                new TestLocation("Polo Nord", 90, 0),
                new TestLocation("Polo Sud", -90, 0),
                new TestLocation("Polo Est (180° longitudine)", 0, 180),
                new TestLocation("Polo Ovest (-180° longitudine)", 0, -180)
        };

        // Esegui test per ogni location
        for (TestLocation location : testLocations) {
            testSingleLocation(location);
        }
    }

    private void testSingleLocation(TestLocation location) {
        // Converti coordinate geografiche in punto sulla sfera
        Point3D spherePoint = geographicToSphereCoordinates(
                location.latitude, location.longitude);

        // Converti il punto della sfera nuovamente in coordinate geografiche
        double[] convertedCoords = sphereCoordinatesToGeographic(spherePoint);

        // Stampa risultati del test
        System.out.println("Test Location: " + location.description);
        System.out.printf("Original Coordinates: Lat %.2f, Lon %.2f%n", location.latitude, location.longitude);
        System.out.printf("Converted Coordinates: Lat %.2f, Lon %.2f%n", convertedCoords[0], convertedCoords[1]);

        // Calcola e stampa la differenza
        double latDiff = Math.abs(location.latitude - convertedCoords[0]);
        double lonDiff = Math.abs(location.longitude - convertedCoords[1]);
        System.out.printf("Difference - Lat: %.6f, Lon: %.6f%n", latDiff, lonDiff);

        // Verifica che la differenza sia molto piccola
        boolean isAccurate = latDiff < 0.01 && lonDiff < 0.01;
        System.out.println("Mapping Accurate: " + isAccurate + "\n");
    }

    // Metodo per convertire coordinate geografiche in punto sulla sfera
    private Point3D geographicToSphereCoordinates(double latitude, double longitude) {
        double radius = sphere.getRadius();

        // Conversione coordinate geografiche in coordinate cartesiane
        double phi = Math.toRadians(latitude);
        double theta = Math.toRadians(longitude);

        double x = radius * Math.cos(phi) * Math.sin(theta);
        double y = -radius * Math.sin(phi);
        double z = -radius * Math.cos(phi) * Math.cos(theta);

        return new Point3D(x, y, z);
    }

    // Metodo per convertire punto sulla sfera in coordinate geografiche
    private double[] sphereCoordinatesToGeographic(Point3D point) {
        double radius = sphere.getRadius();

        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();

        // Calcolo della latitudine
        double latitude = -Math.toDegrees(Math.asin(y / radius));

        // Calcolo della longitudine
        double longitude = Math.toDegrees(Math.atan2(x, -z));

        // Correzione per la longitudine a 180° esatta
        if (Math.abs(longitude - 180) < 1e-6) {
            longitude = 180;
        } else if (Math.abs(longitude + 180) < 1e-6) {
            longitude = -180;
        }

        return new double[]{latitude, longitude};
    }


    // Classe interna per memorizzare i test location
    private static class TestLocation {
        String description;
        double latitude;
        double longitude;

        TestLocation(String description, double latitude, double longitude) {
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
