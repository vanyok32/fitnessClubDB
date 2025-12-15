package fitness.club.controller;

import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.service.AuthService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthController extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        switch (path) {
            case "/login" -> handleLogin(req, resp);
            case "/register" -> handleRegister(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown auth endpoint");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginRequest loginRequest = JsonUtil.read(req, LoginRequest.class);
        if (loginRequest.email == null || loginRequest.password == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email and password are required");
            return;
        }
        JsonUtil.write(resp, HttpServletResponse.SC_OK, authService.login(loginRequest.email, loginRequest.password));
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClientRequestDto dto = JsonUtil.read(req, ClientRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, authService.register(dto));
    }

    private static class LoginRequest {
        public String email;
        public String password;
    }
}
