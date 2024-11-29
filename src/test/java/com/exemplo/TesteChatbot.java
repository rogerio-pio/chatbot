package com.exemplo;

import okhttp3.*;

import java.util.Scanner;

public class TesteChatbot {
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/"; // Substitua pelo endpoint correto
    private static final String API_KEY = "sua-chave-api-aqui";

    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Chatbot! Digite sua mensagem:");

        while (true) {
            System.out.print("Você: ");
            String userInput = scanner.nextLine();

            // Verificar se o usuário quer sair
            if (userInput.equalsIgnoreCase("sair")) {
                System.out.println("Chatbot encerrado. Até mais!");
                break;
            }

            // Criar a requisição para a API
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    "{\"message\":\"" + userInput + "\"}" // Ajuste conforme a estrutura da API
            );

            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .post(body)
                    .build();

            // Enviar a requisição
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Chatbot: " + response.body().string());
                } else {
                    System.err.println("Erro no chatbot: " + response.code());
                }
            }
        }

        scanner.close();
    }
}
