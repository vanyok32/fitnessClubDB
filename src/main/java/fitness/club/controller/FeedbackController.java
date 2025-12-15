package fitness.club.controller;

import fitness.club.entity.Feedback;
import fitness.club.service.FeedbackService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;


@WebServlet("/api/feedbacks/*")
public class FeedbackController extends HttpServlet {
    private final FeedbackService service = new FeedbackService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> id = extractId(req);
        if (id.isPresent()) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findByScheduleId(id.get()));
        } else {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, Feedback.provider.findAll());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Feedback feedback = JsonUtil.read(req, Feedback.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, service.save(feedback));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Feedback feedback = JsonUtil.read(req, Feedback.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, service.update(feedback));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("scheduleId is required"));
        service.delete(id);
        JsonUtil.write(resp, HttpServletResponse.SC_NO_CONTENT, null);
    }

    private Optional<Integer> extractId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isBlank() || "/".equals(pathInfo)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(pathInfo.replace("/", "")));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
