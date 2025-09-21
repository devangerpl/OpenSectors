package io.github.fajzu.common.updater.fetcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VersionDataFetcher {
    public static String fetch(Logger logger,
                               String urlString) {
        if (urlString == null) return "0.0";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                logger.log(Level.SEVERE, "Received non-OK response " + responseCode);
                return "0.0";
            }


            try (InputStream inputStream = connection.getInputStream();
                 InputStreamReader reader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {

                String line = bufferedReader.readLine();
                if (line == null || line.isEmpty()) return "0.0";

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(line);

                if (!element.isJsonArray()) return "0.0";

                JsonArray array = element.getAsJsonArray();
                if (array.size() == 0) return "0.0";

                JsonElement firstTag = array.get(0);
                if (!firstTag.isJsonObject()) return "0.0";

                JsonElement nameElem = firstTag.getAsJsonObject().get("name");
                if (nameElem == null) return "0.0";

                return nameElem.getAsString();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fetching latest GitHub tag failed: " + e.getMessage());
            return "0.0";
        }
    }
}
