package com.edudeveloper.currencyconvert;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CurrencyConverter {

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException, InterruptedException {

        String apiKey = EnvReader.getApiKey();
        
        Scanner scanner = new Scanner(System.in);
        
        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("Por favor, defina a variável de ambiente EXCHANGERATE_API_KEY com sua chave de API.");
            return;
        }
        
        System.out.println("Bem-vindo ao Conversor de Moedas!\n");
        System.out.println("*Digite o símbolo de cada moeda!*\n");
        System.out.println("****Exemplo: de USD para BRL!****\n");
        System.out.println("USD: Dólar Americano");
        System.out.println("EUR: Euro");
        System.out.println("ARS: Peso Argentino");
        System.out.println("BRL: Real Brasileiro");
        System.out.println("CLP: Peso Chileno");
        System.out.println("COP: Peso Colombiano");
        System.out.println("Continua............\n");

        System.out.println("Converter de:");
        String fromCurrency = scanner.nextLine().toUpperCase();
        System.out.println("Converter para:");
        String toCurrency = scanner.nextLine().toUpperCase();
        System.out.println("Quantidade:");
        BigDecimal amount = scanner.nextBigDecimal();
        
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + fromCurrency;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .build();
       
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); 
        
        if (response.statusCode() == 200) {
            String responseBody = response.body();
            Gson gson = new Gson();
            JsonObject exchangeRates = gson.fromJson(responseBody, JsonObject.class);
            JsonObject rates = exchangeRates.getAsJsonObject("conversion_rates");
            
            if (rates.has(toCurrency)) {
                BigDecimal exchangeRate = rates.get(toCurrency).getAsBigDecimal();
                BigDecimal convertedAmount = amount.multiply(exchangeRate);
                System.out.println(amount + " " + fromCurrency + " equivale a " + convertedAmount + " " + toCurrency);
            } else {
                System.out.println("A moeda de destino especificada não é suportada.");
            }
        } else {
            System.out.println("Erro ao fazer a solicitação. Código de status: " + response.statusCode());
        }
        
        scanner.close();
    }
}