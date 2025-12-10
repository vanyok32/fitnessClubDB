package fitness.club.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitness.club.entity.Membership;
import fitness.club.exeptions.ServiceException;
import fitness.club.service.MembershipService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/api/memberships/*")
public class MembershipController extends HttpServlet {
    private final MembershipService membershipService = new MembershipService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String clientId = request.getParameter("clientId");

            if (pathInfo == null || pathInfo.equals("/")) {
                if (clientId != null) {
                    // GET /api/memberships?clientId={clientId}
                    Integer clientIdInt = Integer.parseInt(clientId);
                    JsonUtil.sendJsonResponse(response, membershipService.findByClientId(clientIdInt));
                } else {
                    // GET /api/memberships - получить все членства
                    JsonUtil.sendJsonResponse(response, membershipService.findAll());
                }
            } else if (pathInfo.matches("/\\d+/activate")) {
                // POST /api/memberships/{id}/activate - активировать членство
                Integer id = Integer.parseInt(pathInfo.substring(1, pathInfo.lastIndexOf("/")));
                JsonUtil.sendJsonResponse(response, membershipService.activate(id));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                // POST /api/memberships - создать членство
                String json = readRequestBody(request);
                Membership membership = objectMapper.readValue(json, Membership.class);
                JsonUtil.sendJsonResponse(response, membershipService.save(membership));
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else if (pathInfo.matches("/\\d+/activate")) {
                // POST /api/memberships/{id}/activate - активировать членство
                Integer id = Integer.parseInt(pathInfo.substring(1, pathInfo.lastIndexOf("/")));
                JsonUtil.sendJsonResponse(response, membershipService.activate(id));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                // PUT /api/memberships - обновить членство
                String json = readRequestBody(request);
                Membership membership = objectMapper.readValue(json, Membership.class);
                JsonUtil.sendJsonResponse(response, membershipService.update(membership));
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
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
                // DELETE /api/memberships/{id} - удалить членство
                Integer id = Integer.parseInt(pathInfo.substring(1));
                membershipService.delete(id);
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
            }
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (var reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
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


