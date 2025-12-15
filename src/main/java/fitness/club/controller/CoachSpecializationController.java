package fitness.club.controller;

import fitness.club.entity.CoachSpecialization;
import fitness.club.service.CoachSpecializationService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/coach-specializations/*")
public class CoachSpecializationController extends HttpServlet {
    private final CoachSpecializationService service = new CoachSpecializationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CoachSpecialization dto = JsonUtil.read(req, CoachSpecialization.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, service.save(dto));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CoachSpecialization dto = JsonUtil.read(req, CoachSpecialization.class);
        service.delete(dto);
        JsonUtil.write(resp, HttpServletResponse.SC_NO_CONTENT, null);
    }
}

