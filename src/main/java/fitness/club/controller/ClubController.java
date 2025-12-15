package fitness.club.controller;

import fitness.club.dto.clubDto.ClubRequestDto;
import fitness.club.service.ClubService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/clubs/*")
public class ClubController extends HttpServlet {
    private final ClubService clubService = new ClubService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> id = extractId(req);
        if (id.isPresent()) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, clubService.getClubById(id.get()));
        } else {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, clubService.getAllClubs());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClubRequestDto dto = JsonUtil.read(req, ClubRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, clubService.save(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        ClubRequestDto dto = JsonUtil.read(req, ClubRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, clubService.update(dto, id));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        clubService.deleteClub(id);
        JsonUtil.write(resp, HttpServletResponse.SC_NO_CONTENT, null);
    }

    private Optional<Integer> extractId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(pathInfo.replace("/", "")));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
