package com.example.all_about_earth_.API;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class API {

    private final static String MAPS_API_KEY = "AIzaSyDA-cz4lKPKW4XS3iVHKX5qtStLBmsOw9w";
    private static final String GEMINI_API_KEY = "AIzaSyDGV9CmAf7cJDGs--3vpyecsgrMJLmVCEo";
    private static final String ELEVENLABS_API_KEY = "sk_fd88bccbb515b640bf2f127b79007200fecefe7930644d3e";// toDo controllare la API key prima di consegnare
    private double latitude, longitude;
    private String writtenSpeech;
    private ByteArrayInputStream spokenSpeech;
    private String place_name;
    private String place_id;

    /*public static void main(String[] args) {
        String response = sendPrompt();


        System.out.println(temp[0] + temp[1]);

        String place_id = getPlaceId(place_name);
        if (place_id != null) {
            getPlacePhotos(place_id);
        } else {
            System.out.println("Nessun place_id trovato.");
        }

    }*/

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

    public void sendPrompt() {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            JSONObject part = new JSONObject();
            part.put("text", "Tell me a mix of trivia and history at the nearest city at " + latitude + " " + longitude + " coordinates, if there's nothing interesting you can tell me a general mix for the location at the coordinates, respond only with a human type speech, that start with the name like the example Norrköping, Sweden : of max 100 words talking about the trivia and history, ALL IN ITALIAN EXCEPT THE NAME OF THE CITY AND THE NAME OF THE STATE");

            JSONArray partsArray = new JSONArray();
            partsArray.put(part);

            JSONObject content = new JSONObject();
            content.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(content);

            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", contentsArray);

            RequestBody body = RequestBody.create(
                    MediaType.get("application/json; charset=utf-8"),
                    requestBody.toString()
            );

            Request request = new Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("Errore nella richiesta: " + response.code());
                }

                assert response.body() != null;
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);

                //Parsing
                JSONArray candidates = jsonResponse.optJSONArray("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    JSONObject firstCandidate = candidates.getJSONObject(0);
                    JSONObject content0 = firstCandidate.getJSONObject("content");
                    JSONArray parts = content0.getJSONArray("parts");
                    if (!parts.isEmpty()) {
                        JSONObject firstPart = parts.getJSONObject(0);
                        String[] temp = firstPart.getString("text").split(":", 2);
                        place_name = temp[0];
                        getPlaceId();
                        writtenSpeech = temp[0];
                        //getSpeech(); toDo
                        return;
                    }
                }
                System.out.println("Nessuna risposta disponibile.");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSpeech() {

        JSONObject json = new JSONObject();
        json.put("text", writtenSpeech);
        json.put("model_id", "eleven_multilingual_v2");

        try {
            HttpResponse<InputStream> mp3 = Unirest.post("https://api.elevenlabs.io/v1/text-to-speech/JBFqnCBsd6RMkjVDRZzb?output_format=mp3_44100_128")
                    .header("xi-api-key", ELEVENLABS_API_KEY)
                    .header("Content-Type", "application/json")
                    .body(json.toString())
                    .asBinary();

            if (mp3.getStatus() == 200) {
                spokenSpeech = new ByteArrayInputStream(mp3.getBody().readAllBytes());
                /*Player player = new Player(bis);
                player.play();*/
            } else {
                System.out.println("Errore nella richiesta API: " + mp3.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPlaceId(){
        try{
            OkHttpClient client = new OkHttpClient();
            String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" + "input=" + place_name.replace(" ", "%20") + "&inputtype=textquery&fields=place_id&key=" + MAPS_API_KEY;

            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String jsonResponse = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray candidates = jsonObject.getJSONArray("candidates");

            if (!candidates.isEmpty()) {
                place_id = candidates.getJSONObject(0).getString("place_id");
                System.out.println(place_id);
                System.out.println(place_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getPlacePhotos(){
        String[] photo = new String[10];
        try{
            OkHttpClient client = new OkHttpClient();
            String url = "https://maps.googleapis.com/maps/api/place/details/json?" + "place_id=" + place_id + "&fields=photos&key=" + MAPS_API_KEY;
            System.out.println(url);
            System.out.println(place_id);
            System.out.println(place_name);


            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            String jsonResponse = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject result = jsonObject.getJSONObject("result");

            if (result.has("photos")) {
                JSONArray photos = result.getJSONArray("photos");
                int count = Math.min(photos.length(), 10); // Prende massimo 4 foto

                for (int i = 0; i < count; i++) {
                    String photoReference = photos.getJSONObject(i).getString("photo_reference");
                    photo[i] = "https://maps.googleapis.com/maps/api/place/photo?" + "maxwidth=800&photo_reference=" + photoReference + "&key=" + MAPS_API_KEY;
                }
            } else {
                System.out.println("Nessuna foto trovata.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ByteArrayInputStream getSpokenSpeech() {
        return spokenSpeech;
    }

    public String getWrittenSpeech() {
        return writtenSpeech;
    }

    public String getPlace_name() {
        return place_name;
    }
}