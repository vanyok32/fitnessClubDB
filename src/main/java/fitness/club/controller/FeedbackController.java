package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.entity.Feedback;
import fitness.club.exeptions.ServiceException;
import fitness.club.service.FeedbackService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/feedback/*")
public class FeedbackController extends HttpServlet {
    private final FeedbackService feedbackService = new FeedbackService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String scheduleId = request.getParameter("scheduleId");

            if (scheduleId != null) {
                // GET /api/feedback?scheduleId={scheduleId}
                Integer scheduleIdInt = Integer.parseInt(scheduleId);
                JsonUtil.sendJsonResponse(response, feedbackService.findByScheduleId(scheduleIdInt));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "scheduleId parameter is required");
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
                // POST /api/feedback - создать отзыв
                String json = readRequestBody(request);
                Feedback feedback = objectMapper.readValue(json, Feedback.class);
                JsonUtil.sendJsonResponse(response, feedbackService.save(feedback));
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
            if (pathInfo == null || pathInfo.equals("/")) {
                // PUT /api/feedback - обновить отзыв
                String json = readRequestBody(request);
                Feedback feedback = objectMapper.readValue(json, Feedback.class);
                JsonUtil.sendJsonResponse(response, feedbackService.update(feedback));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String scheduleId = request.getParameter("scheduleId");
            if (scheduleId != null) {
                // DELETE /api/feedback?scheduleId={scheduleId}
                Integer scheduleIdInt = Integer.parseInt(scheduleId);
                feedbackService.delete(scheduleIdInt);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "scheduleId parameter is required");
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


