package fitness.club.controller;

import fitness.club.dto.coachDto.CoachRequestDto;
import fitness.club.service.ClubService;
import fitness.club.service.CoachService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/coaches/*")
public class CoachController extends HttpServlet {
    private final CoachService coachService = new CoachService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> id = extractId(req);
        if (id.isPresent()) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, coachService.findById(id.get()));
            return;
        }

        Integer clubId = parseInteger(req.getParameter("clubId"));
        Integer specId = parseInteger(req.getParameter("specId"));
        String sort = req.getParameter("sort");
        JsonUtil.write(resp, HttpServletResponse.SC_OK, coachService.findWithFilters(clubId, specId, sort));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CoachRequestDto dto = JsonUtil.read(req, CoachRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, coachService.save(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        CoachRequestDto dto = JsonUtil.read(req, CoachRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, coachService.update(dto, id));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        coachService.delete(id);
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

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
