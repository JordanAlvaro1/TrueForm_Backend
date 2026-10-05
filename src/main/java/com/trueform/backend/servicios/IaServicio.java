package com.trueform.backend.servicios;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trueform.backend.dto.IaRequestDTO;
import com.trueform.backend.dto.IaResponseDTO;
import com.trueform.backend.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class IaServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public IaResponseDTO responderPregunta(IaRequestDTO request) {

        if (request == null) {
            return new IaResponseDTO(
                    null,
                    null,
                    null,
                    false,
                    "Debes ingresar una pregunta."
            );
        }

        if (request.getIdUsuario() == null) {
            return null;
        }

        Usuario usuario =
                usuarioServicio.buscarPorId(request.getIdUsuario());

        if (usuario == null) {
            return null;
        }

        if (request.getPregunta() == null ||
                request.getPregunta().trim().isEmpty()) {

            return new IaResponseDTO(
                    request.getIdUsuario(),
                    request.getPregunta(),
                    null,
                    false,
                    "Debes ingresar una pregunta."
            );
        }

        String pregunta = request.getPregunta().trim();

        try {

            RestClient restClient = RestClient.create();

            String prompt =
                    "Eres el asistente virtual de TrueForm, una aplicación de entrenamiento físico. " +
                            "Responde en español de manera clara, breve y educativa. " +
                            "Responde principalmente dudas sobre ejercicios, técnica, entrenamiento, " +
                            "descanso y recuperación. " +
                            "No reemplaces la evaluación de un médico o profesional de la salud. " +
                            "Si el usuario menciona una lesión, dolor intenso o una emergencia, " +
                            "recomienda consultar a un profesional de salud.\n\n" +
                            "Pregunta del usuario: " + pregunta;

            Map<String, Object> textPart =
                    Map.of("text", prompt);

            Map<String, Object> content =
                    Map.of("parts", List.of(textPart));

            Map<String, Object> body =
                    Map.of("contents", List.of(content));

            String respuestaApi = restClient.post()
                    .uri(apiUrl)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            JsonNode json = objectMapper.readTree(respuestaApi);

            String respuesta = extraerRespuesta(json);

            if (respuesta == null || respuesta.isBlank()) {

                return new IaResponseDTO(
                        request.getIdUsuario(),
                        pregunta,
                        null,
                        false,
                        "La IA no pudo generar una respuesta."
                );
            }

            return new IaResponseDTO(
                    request.getIdUsuario(),
                    pregunta,
                    respuesta,
                    true,
                    "Consulta respondida correctamente."
            );

        } catch (Exception e) {

            System.err.println(
                    "Error al consultar Gemini: " + e.getMessage()
            );

            return new IaResponseDTO(
                    request.getIdUsuario(),
                    pregunta,
                    null,
                    false,
                    "No fue posible conectar con el asistente de IA."
            );
        }
    }

    private String extraerRespuesta(JsonNode json) {

        JsonNode candidates = json.path("candidates");

        if (!candidates.isArray() || candidates.isEmpty()) {
            return null;
        }

        JsonNode parts = candidates
                .get(0)
                .path("content")
                .path("parts");

        if (!parts.isArray() || parts.isEmpty()) {
            return null;
        }

        return parts.get(0).path("text").asText();
    }
}