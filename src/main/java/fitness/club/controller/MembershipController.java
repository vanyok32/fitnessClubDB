package fitness.club.controller;

import fitness.club.entity.Membership;
import fitness.club.service.MembershipService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/memberships/*")
public class MembershipController extends HttpServlet {
    private final MembershipService service = new MembershipService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> id = extractId(req);
        if (id.isPresent()) {
            try {
                JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findByClientId(id.get()));
            } catch (fitness.club.exeptions.ServiceException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        } else {
            JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findAll());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Membership membership = JsonUtil.read(req, Membership.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, service.save(membership));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Membership membership = JsonUtil.read(req, Membership.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, service.update(membership));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("clientId is required"));
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
