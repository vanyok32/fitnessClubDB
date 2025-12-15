package fitness.club.controller;

import fitness.club.dto.scheduleDto.ScheduleRequestDto;
import fitness.club.mapper.ScheduleMapper;
import fitness.club.service.ScheduleService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/api/schedules/*")
public class ScheduleController extends HttpServlet {
    private final ScheduleService scheduleService = new ScheduleService();
    private final ScheduleMapper mapper = new ScheduleMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String coachId = req.getParameter("coachId");
        String clientId = req.getParameter("clientId");
        if (coachId != null) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, scheduleService.findByCoachId(Integer.parseInt(coachId)));
            return;
        }
        if (clientId != null) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, scheduleService.findByClientId(Integer.parseInt(clientId)));
            return;
        }
        // findAll возвращает List<Schedule>, преобразуем в DTO
        JsonUtil.write(resp, HttpServletResponse.SC_OK,
                scheduleService.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ScheduleRequestDto dto = JsonUtil.read(req, ScheduleRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, scheduleService.save(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        ScheduleRequestDto dto = JsonUtil.read(req, ScheduleRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, scheduleService.update(dto, id));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        scheduleService.delete(id);
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
