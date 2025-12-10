package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.dto.clubDto.ClubRequestDto;
import fitness.club.exeptions.ServiceException;
import fitness.club.service.ClubService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/clubs/*")
public class ClubController extends HttpServlet {
    private final ClubService clubService = new ClubService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /api/clubs - получить все клубы
                JsonUtil.sendJsonResponse(response, clubService.getAllClubs());
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/clubs/{id} - получить клуб по ID
                Integer id = Integer.parseInt(pathInfo.substring(1));
                JsonUtil.sendJsonResponse(response, clubService.getClubById(id));
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
                // POST /api/clubs - создать клуб
                String json = readRequestBody(request);
                ClubRequestDto dto = objectMapper.readValue(json, ClubRequestDto.class);
                JsonUtil.sendJsonResponse(response, clubService.save(dto));
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
                // PUT /api/clubs/{id} - обновить клуб
                Integer id = Integer.parseInt(pathInfo.substring(1));
                String json = readRequestBody(request);
                ClubRequestDto dto = objectMapper.readValue(json, ClubRequestDto.class);
                JsonUtil.sendJsonResponse(response, clubService.update(dto, id));
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
                // DELETE /api/clubs/{id} - удалить клуб
                Integer id = Integer.parseInt(pathInfo.substring(1));
                clubService.deleteClub(id);
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

