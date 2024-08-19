import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class exchangeRateFetcher {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/EUR";

    public static double getExchangeRate(String currencyCode) throws Exception {
        // Erstelle eine URL-Instanz
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Lese die Antwort von der API
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        // Verwende GSON, um die JSON-Antwort zu parsen
        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonObject rates = jsonResponse.getAsJsonObject("rates");

        // Prüfe, ob der Währungscode vorhanden ist und gebe den Wechselkurs zurück
        if (rates.has(currencyCode)) {
            return rates.get(currencyCode).getAsDouble();
        } else {
            throw new Exception("Währungscode " + currencyCode + " nicht gefunden.");
        }
    }
}
