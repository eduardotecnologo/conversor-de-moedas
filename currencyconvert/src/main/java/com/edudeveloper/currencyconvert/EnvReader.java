package com.edudeveloper.currencyconvert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvReader {
    private static final String ENV_FILE_PATH = "src/main/resources/config/.env";

    public static String getApiKey() throws IOException {
        Map<String, String> envMap = readEnv();
        return envMap.get("API_KEY");
    }

    public static Map<String, String> readEnv() throws IOException {
        Map<String, String> envMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE_PATH));
        String line;

        while((line = reader.readLine()) != null){
            String[] parts = line.split("=", 2);
            if(parts.length >= 2){
                String key = parts[0].trim();
                String value = parts[1].trim();
                envMap.put(key, value);
            }
        }
        reader.close();
        return envMap;
        }
}
