package com.example.all_about_earth_.API;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Request {

    private final static String MAPS_API_KEY = "AIzaSyDA-cz4lKPKW4XS3iVHKX5qtStLBmsOw9w";

    public static void main(String[] args) {
        double latitude = 58;
        double longitude = 18;
        ArrayList<String> nearbyCities = findNearestLocations(latitude, longitude);

        System.out.println("Luoghi più vicini:");
        for (String location : nearbyCities) {
            System.out.println(location);
        }
    }

    public static ArrayList<String> findNearestLocations(double lat, double lng) {
        ArrayList<String> locations = new ArrayList<>();

        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + MAPS_API_KEY;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray results = jsonResponse.getAsJsonArray("results");

            // Stampa l'intera risposta JSON per debug completo
            System.out.println("Risposta JSON completa: " + jsonResponse);

            if (results != null && results.size() > 0) {
                for (JsonElement resultElement : results) {
                    JsonObject result = resultElement.getAsJsonObject();

                    if (result.has("formatted_address")) {
                        String formattedAddress = result.get("formatted_address").getAsString();
                        locations.add(formattedAddress);
                    }

                    if (result.has("address_components")) {
                        JsonArray addressComponents = result.getAsJsonArray("address_components");
                        for (JsonElement componentElement : addressComponents) {
                            JsonObject component = componentElement.getAsJsonObject();
                            JsonArray types = component.getAsJsonArray("types");

                            for (JsonElement typeElement : types) {
                                String type = typeElement.getAsString();

                                if (type.equals("country") ||
                                        type.equals("administrative_area_level_1") ||
                                        type.equals("locality") ||
                                        type.equals("postal_town")) {

                                    String locationName = component.get("long_name").getAsString();
                                    if (!locations.contains(locationName)) {
                                        locations.add(locationName);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (locations.isEmpty()) {
            locations.add("Nessun luogo identificato per le coordinate specificate.");
        }

        return locations;
    }
}