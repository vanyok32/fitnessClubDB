package fitness.club.controller;

import fitness.club.dto.workoutDto.WorkoutRequestDto;
import fitness.club.service.WorkoutService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/workouts/*")
public class WorkoutController extends HttpServlet {
    private final WorkoutService workoutService = new WorkoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> id = extractId(req);
        if (id.isPresent()) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, workoutService.findById(id.get()));
        } else {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, workoutService.findAll());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WorkoutRequestDto dto = JsonUtil.read(req, WorkoutRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, workoutService.save(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        WorkoutRequestDto dto = JsonUtil.read(req, WorkoutRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, workoutService.update(dto, id));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        workoutService.delete(id);
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
