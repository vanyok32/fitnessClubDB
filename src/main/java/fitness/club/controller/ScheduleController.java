package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.dto.scheduleDto.ScheduleRequestDto;
import fitness.club.exeptions.ServiceException;
import fitness.club.service.ScheduleService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/schedules/*")
public class ScheduleController extends HttpServlet {
    private final ScheduleService scheduleService = new ScheduleService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String coachId = request.getParameter("coachId");
            String clientId = request.getParameter("clientId");

            if (pathInfo == null || pathInfo.equals("/")) {
                if (coachId != null) {
                    // GET /api/schedules?coachId={coachId}
                    Integer coachIdInt = Integer.parseInt(coachId);
                    JsonUtil.sendJsonResponse(response, scheduleService.findByCoachId(coachIdInt));
                } else if (clientId != null) {
                    // GET /api/schedules?clientId={clientId}
                    Integer clientIdInt = Integer.parseInt(clientId);
                    JsonUtil.sendJsonResponse(response, scheduleService.findByClientId(clientIdInt));
                } else {
                    // GET /api/schedules - получить все расписания
                    JsonUtil.sendJsonResponse(response, scheduleService.findAll());
                }
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/schedules/{id} - получить расписание по ID
                Integer id = Integer.parseInt(pathInfo.substring(1));
                JsonUtil.sendJsonResponse(response, scheduleService.findById(id));
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
                // POST /api/schedules - создать расписание
                String json = readRequestBody(request);
                ScheduleRequestDto dto = objectMapper.readValue(json, ScheduleRequestDto.class);
                JsonUtil.sendJsonResponse(response, scheduleService.save(dto));
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
                // PUT /api/schedules/{id} - обновить расписание
                Integer id = Integer.parseInt(pathInfo.substring(1));
                String json = readRequestBody(request);
                ScheduleRequestDto dto = objectMapper.readValue(json, ScheduleRequestDto.class);
                JsonUtil.sendJsonResponse(response, scheduleService.update(dto, id));
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
                // DELETE /api/schedules/{id} - удалить расписание
                Integer id = Integer.parseInt(pathInfo.substring(1));
                scheduleService.delete(id);
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

