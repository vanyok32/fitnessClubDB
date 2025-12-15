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
        String clientIdParam = req.getParameter("clientId");
        if (clientIdParam == null || clientIdParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client id is required");
            return;
        }
        try {
            int clientId = Integer.parseInt(clientIdParam);
            JsonUtil.write(resp, HttpServletResponse.SC_OK, scheduleService.findByClientId(clientId));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client id");
        }
    }
}

