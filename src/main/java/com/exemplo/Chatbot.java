package com.exemplo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chatbot {
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    // Substitua pela sua chave de API
    private static final String API_KEY = "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";
    private static final String MODEL_NAME = "llama3-8b-8192";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public Chatbot() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     *
     * @param userMessage A mensagem enviada ao chatbot.
     * @return A resposta do chatbot.
     */
    public String enviarMensagem(String userMessage) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("user", userMessage));

            ChatRequest chatRequest = new ChatRequest(MODEL_NAME, messages);
            String jsonRequest = objectMapper.writeValueAsString(chatRequest);

            RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = objectMapper.readValue(response.body().string(), ChatResponse.class);
                    return chatResponse.getFirstResponse();
                } else {
                    return "Erro: " + (response.body() != null ? response.body().string() : "Resposta vazia.");
                }
            }
        } catch (IOException e) {
            return "Erro ao enviar mensagem: " + e.getMessage();
        }
    }

    /**
     * Classe principal para executar o chatbot.
     */
    public static void main(String[] args) {
        Chatbot chatbot = new Chatbot();
        System.out.println("Chatbot iniciado. Digite sua mensagem:");

        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            while (true) {
                System.out.print("Você: ");
                String userMessage = scanner.nextLine();

                if ("sair".equalsIgnoreCase(userMessage)) {
                    System.out.println("Encerrando o chatbot...");
                    break;
                }

                String botResponse = chatbot.enviarMensagem(userMessage);
                System.out.println("Chatbot: " + botResponse);
            }
        }
    }


    public static class ChatRequest {
        private final String model;
        private final List<Message> messages;

        public ChatRequest(String model, List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }

        public String getModel() {
            return model;
        }

        public List<Message> getMessages() {
            return messages;
        }
    }

    public static class Message {
        private String role;
        private String content;

        public Message() {
        }

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        // Getters e setters
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


    // Classe para representar a resposta JSON
    // Classe para representar a resposta JSON
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChatResponse {
        private List<Choice> choices;

        public ChatResponse() {
        }

        public List<Choice> getChoices() {
            return choices;
        }

        public void setChoices(List<Choice> choices) {
            this.choices = choices;
        }

        public String getFirstResponse() {
            if (choices != null && !choices.isEmpty()) {
                Message assistantMessage = choices.get(0).getMessage();
                return assistantMessage != null ? assistantMessage.getContent() : "Sem resposta.";
            }
            return "Sem resposta.";
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private int index;
        private Message message;

        public Choice() {
        }

        // Getters e setters
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }




}