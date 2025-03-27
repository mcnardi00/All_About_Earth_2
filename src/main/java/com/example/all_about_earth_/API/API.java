package com.example.all_about_earth_.API;

import com.example.all_about_earth_.Applications.Error;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import javafx.stage.Stage;
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

    public void sendPrompt() {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            JSONObject part = new JSONObject();
            part.put("text", "Genera un testo informativo in formato [Nome della città], [Paese] che includa:\n" +
                    "\n" +
                    "Una breve descrizione della località, menzionando eventuali caratteristiche geografiche rilevanti.\n" +
                    "Una sezione storica che descriva brevemente la fondazione o eventi storici significativi legati alla località.\n" +
                    "Una sezione \"Curiosità\" che presenti uno o due fatti interessanti o insoliti sulla località.\n" +
                    "Utilizza le seguenti coordinate geografiche come riferimento per la località: " + latitude + ", " + longitude + "\n Rispondi solo con il testo informativo");

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
                    Error error = new Error("Errore nella richiesta");
                    error.start(new Stage());
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
                        String[] temp = firstPart.getString("text").split("\n", 2);
                        place_name = temp[0];
                        getPlaceId();
                        writtenSpeech = temp[0] + temp[1];
                        //getSpeech(); toDo
                        return;
                    }
                }
                Error error = new Error("Nessuna risposta disponibile.");
                error.start(new Stage());
            }
        }catch (Exception e) {}
    }

    public void sendPrompt(String place) {
        OkHttpClient httpClient = new OkHttpClient();
        try {
            JSONObject part = new JSONObject();
            part.put("text", "Genera un testo informativo in formato [Nome della città], [Paese] che includa:\n" +
                    "\n" +
                    "Una breve descrizione della località, menzionando eventuali caratteristiche geografiche rilevanti.\n" +
                    "Una sezione \"Storia\" che descriva brevemente la fondazione o eventi storici significativi legati alla località.\n" +
                    "Una sezione \"Curiosità\" che presenti uno o due fatti interessanti o insoliti sulla località.\n" +
                    "Utilizza il seguente nome come riferimento per la località: " + place + "\n Rispondi solo con il testo informativo o con la stringa Il luogo inserito non è valido");

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
                    Error error = new Error("Errore nella richiesta");
                    error.start(new Stage());
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
                        String[] temp = firstPart.getString("text").split("\n", 2);
                        place_name = temp[0];
                        getPlaceId();
                        writtenSpeech = temp[0] + temp[1];
                        //getSpeech(); toDo
                        return;
                    }
                }
                System.out.println("Nessuna risposta disponibile.");
                Error error = new Error("Nessuna risposta disponibile.");
                error.start(new Stage());
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
            } else {
                System.out.println("Errore nella richiesta API: " + mp3.getStatus());
                Error error = new Error("Errore nella richiesta API");
                error.start(new Stage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPlaceId(){
        try{
            place_name = place_name.replace("*","");
            if (!place_name.equals("Il luogo inserito non è valido")){
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
                }
            }else {
                place_id = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getPlacePhotos(){
        String[] photo = new String[10];
        System.out.println(place_name);
        System.out.println(place_id);
        try{
            if (place_id != null){
                OkHttpClient client = new OkHttpClient();
                String url = "https://maps.googleapis.com/maps/api/place/details/json?" + "place_id=" + place_id + "&fields=photos&key=" + MAPS_API_KEY;

                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                assert response.body() != null;
                String jsonResponse = response.body().string();

                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONObject result = jsonObject.getJSONObject("result");

                if (result.has("photos")) {
                    JSONArray photos = result.getJSONArray("photos");
                    int count = Math.min(photos.length(), 10);

                    for (int i = 0; i < count; i++) {
                        String photoReference = photos.getJSONObject(i).getString("photo_reference");
                        photo[i] = "https://maps.googleapis.com/maps/api/place/photo?" + "maxwidth=1050&photo_reference=" + photoReference + "&key=" + MAPS_API_KEY;
                    }
                } else {
                    System.out.println("Nessuna foto trovata.");
                    Error error = new Error("Nessuna foto trovata.");
                    error.start(new Stage());

                }
            } else {
                Error error = new Error("Nessuna foto trovata.");
                error.start(new Stage());
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