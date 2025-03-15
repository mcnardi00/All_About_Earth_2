package com.example.all_about_earth_.API;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javazoom.jl.player.Player;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class API {

    private final static String MAPS_API_KEY = "AIzaSyDA-cz4lKPKW4XS3iVHKX5qtStLBmsOw9w";
    private static final String GEMINI_API_KEY = "AIzaSyDGV9CmAf7cJDGs--3vpyecsgrMJLmVCEo";
    private static final String ELEVENLABS_API_KEY = "sk_fd88bccbb515b640bf2f127b79007200fecefe7930644d3e";

    public static void main(String[] args) {
        double latitude = 58;
        double longitude = 18;
        String response = sendPrompt("Tell me a mix of trivia and history at the nearest city at " + latitude + " " + longitude + " coordinates, if there's nothing interesting you can tell me a general mix for the location at the coordinates, respond only with a speech of max 100 words talking about the trivia and history");
        System.out.println(response);

        JSONObject json = new JSONObject();
        json.put("text", response);
        json.put("model_id", "eleven_multilingual_v2");

        try {
            HttpResponse<InputStream> mp3 = Unirest.post("https://api.elevenlabs.io/v1/text-to-speech/JBFqnCBsd6RMkjVDRZzb?output_format=mp3_44100_128")
                    .header("xi-api-key", ELEVENLABS_API_KEY)
                    .header("Content-Type", "application/json")
                    .body(json.toString())
                    .asBinary();

            if (mp3.getStatus() == 200) {
                ByteArrayInputStream bis = new ByteArrayInputStream(mp3.getBody().readAllBytes());
                Player player = new Player(bis);
                player.play();
            } else {
                System.out.println("Errore nella richiesta API: " + mp3.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static ArrayList<String> findNearestLocations(double lat, double lng) {
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
    }*/

    public static String sendPrompt(String prompt) {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            // Creazione del corpo della richiesta JSON
            JSONObject part = new JSONObject();
            part.put("text", prompt);

            JSONArray partsArray = new JSONArray();
            partsArray.put(part);

            JSONObject content = new JSONObject();
            content.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(content);

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", contentsArray);

            // Creazione della richiesta HTTP
            RequestBody body = RequestBody.create(
                    MediaType.get("application/json; charset=utf-8"),
                    requestBody.toString()
            );

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY)
                    .post(body)
                    .build();

            // Esecuzione della richiesta e gestione della risposta
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Errore nella richiesta: " + response.code();
                }

                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                // Parsing della risposta di Gemini
                JSONArray candidates = jsonResponse.optJSONArray("candidates");
                if (candidates != null && candidates.length() > 0) {
                    JSONObject firstCandidate = candidates.getJSONObject(0);
                    JSONObject content0 = firstCandidate.getJSONObject("content");
                    JSONArray parts = content0.getJSONArray("parts");
                    if (parts.length() > 0) {
                        JSONObject firstPart = parts.getJSONObject(0);
                        return firstPart.getString("text");
                    }
                }
                return "Nessuna risposta disponibile nel formato atteso.";
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}