package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.dto.specializationDto.SpecializationRequestDto;
import fitness.club.exeptions.ServiceException;
import fitness.club.service.SpecializationService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/specializations/*")
public class SpecializationController extends HttpServlet {
    private final SpecializationService specializationService = new SpecializationService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/specializations - получить все специализации
                JsonUtil.sendJsonResponse(response, specializationService.findAll());
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/specializations/{id} - получить специализацию по ID
                Integer id = Integer.parseInt(pathInfo.substring(1));
                JsonUtil.sendJsonResponse(response, specializationService.findById(id));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                // POST /api/specializations - создать специализацию
                String json = readRequestBody(request);
                SpecializationRequestDto dto = objectMapper.readValue(json, SpecializationRequestDto.class);
                JsonUtil.sendJsonResponse(response, specializationService.save(dto));
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // PUT /api/specializations/{id} - обновить специализацию
                Integer id = Integer.parseInt(pathInfo.substring(1));
                String json = readRequestBody(request);
                SpecializationRequestDto dto = objectMapper.readValue(json, SpecializationRequestDto.class);
                JsonUtil.sendJsonResponse(response, specializationService.update(dto, id));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // DELETE /api/specializations/{id} - удалить специализацию
                Integer id = Integer.parseInt(pathInfo.substring(1));
                specializationService.delete(id);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            }
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
        if (e instanceof ServiceException) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
            e.printStackTrace();
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        JsonUtil.sendErrorResponse(response, status, message);
    }
}


