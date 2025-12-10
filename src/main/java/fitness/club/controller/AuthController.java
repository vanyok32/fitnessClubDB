package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.exeptions.AuthException;
import fitness.club.service.AuthService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthController extends HttpServlet {
    private final AuthService authService = new AuthService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String json = readRequestBody(request);

            if (pathInfo != null && pathInfo.equals("/login")) {
                // POST /api/auth/login - вход
                Map<String, String> loginData = objectMapper.readValue(json, Map.class);
                String email = loginData.get("email");
                if (email == null || email.isEmpty()) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Email is required");
                    return;
                }
                JsonUtil.sendJsonResponse(response, authService.login(email));
            } else if (pathInfo != null && pathInfo.equals("/register")) {
                // POST /api/auth/register - регистрация
                ClientRequestDto dto = objectMapper.readValue(json, ClientRequestDto.class);
                JsonUtil.sendJsonResponse(response, authService.register(dto));
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
            }
        } catch (AuthException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (var reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        e.printStackTrace();
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        JsonUtil.sendErrorResponse(response, status, message);
    }
}


