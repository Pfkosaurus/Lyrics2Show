package com.matus.lyrics.proceed;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageGetter {
    private static final String API_KEY = "oInrIrfGIWTGtRflMpbraL2lIaCpykgsWyerKpTspz0";
    private static final String DOWNLOAD_FOLDER = "C:\\Users\\vha5805\\winapp\\random\\";
    private static int order = 0;

    public static void execute(String lyricsAsOneWord) throws IOException, JSONException {
        String idAndLocation = convertWordToImageId(lyricsAsOneWord);
        if (idAndLocation != null) {
            findAndSave(idAndLocation, lyricsAsOneWord);
        }
    }

    private static String convertWordToImageId(String word) throws IOException, JSONException {
        String apiUrl = "https://api.unsplash.com/search/photos?query=" + word + "&per_page=30";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Client-ID " + API_KEY);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream in = connection.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");
                if (results.length() > 0) {
                    int randomIndex = (int) (Math.random() * results.length());
                    JSONObject randomPhoto = results.getJSONObject(randomIndex);
                    String imageId = randomPhoto.getString("id");
                    JSONObject urls = randomPhoto.getJSONObject("urls");
                    String downloadLocation = urls.getString("raw");
                    return imageId + "*-#####-*" + downloadLocation;
                }
            }
        } else {
            throw new IOException("Failed!!! Return code:" + responseCode);
        }
        return null;
    }

    private static void findAndSave(String idAndLocation, String word) throws IOException {
        String delimiter = "*-#####-*";
        int index = idAndLocation.indexOf(delimiter);
        String location = idAndLocation.substring(index + delimiter.length());
        String apiUrl = location + "&w=200&fit=max";
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Client-ID " + API_KEY);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String disposition = connection.getHeaderField("Content-Disposition");
            String fileName = disposition != null ? disposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1") : order + ".jpg";
            order++;

            try (InputStream in = connection.getInputStream()) {
                File outputFile = new File(DOWNLOAD_FOLDER + fileName);
                try (OutputStream out = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
        } else {
            throw new IOException("Failed!!! Return code:" + responseCode);
        }
    }
}