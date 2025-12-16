package fitness.club.controller;

import fitness.club.service.ScheduleService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/client/workouts/*")
public class ClientWorkoutsController extends HttpServlet {
    private final ScheduleService scheduleService = new ScheduleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // например, "/5" или null

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client ID is required in path");
            return;
        }

        String clientIdStr = pathInfo.substring(1); // убираем ведущий слеш
        try {
            int clientId = Integer.parseInt(clientIdStr);
            var workouts = scheduleService.findByClientId(clientId);
            JsonUtil.write(resp, HttpServletResponse.SC_OK, workouts);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID format");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching workouts");
        }
    }
}