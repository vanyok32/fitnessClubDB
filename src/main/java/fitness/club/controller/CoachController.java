package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.exeptions.ServiceException;
import fitness.club.service.CoachService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/coaches/*")
public class CoachController extends HttpServlet {
    private final CoachService coachService = new CoachService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String clubId = request.getParameter("clubId");
            String specId = request.getParameter("specId");

            if (pathInfo == null || pathInfo.equals("/")) {
                if (clubId != null && specId != null) {
                    // GET /api/coaches?clubId={clubId}&specId={specId}
                    Integer clubIdInt = Integer.parseInt(clubId);
                    Integer specIdInt = Integer.parseInt(specId);
                    JsonUtil.sendJsonResponse(response, coachService.findCoachesBySpecIdClubId(clubIdInt, specIdInt));
                } else if (clubId != null) {
                    // GET /api/coaches?clubId={clubId}
                    Integer clubIdInt = Integer.parseInt(clubId);
                    JsonUtil.sendJsonResponse(response, coachService.findByClub(clubIdInt));
                } else {
                    // GET /api/coaches - получить всех тренеров
                    JsonUtil.sendJsonResponse(response, coachService.findAll());
                }
            } else if (pathInfo.matches("/\\d+")) {
                // GET /api/coaches/{id} - получить тренера по ID
                Integer id = Integer.parseInt(pathInfo.substring(1));
                JsonUtil.sendJsonResponse(response, coachService.findById(id));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
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
                // DELETE /api/coaches/{id} - удалить тренера
                Integer id = Integer.parseInt(pathInfo.substring(1));
                coachService.delete(id);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
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


