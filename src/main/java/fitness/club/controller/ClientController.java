package fitness.club.controller;

import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.entity.Client;
import fitness.club.mapper.ClientMapper;
import fitness.club.service.AuthService;
import fitness.club.service.ClientService;
import fitness.club.util.JsonUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/clients/*")
public class ClientController extends HttpServlet {
    private final ClientService clientService = new ClientService();
    private final AuthService authService = new AuthService(); // используем для создания клиента (регистрация)
    private final ClientMapper mapper = new ClientMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<Integer> id = extractId(req);
        if (id.isPresent()) {
            JsonUtil.write(resp, HttpServletResponse.SC_OK,
                    Client.provider.findById(id.get()).map(mapper::toResponseDto)
                            .orElse(null));
            return;
        }
        JsonUtil.write(resp, HttpServletResponse.SC_OK, clientService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ClientRequestDto dto = JsonUtil.read(req, ClientRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_CREATED, authService.register(dto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        ClientRequestDto dto = JsonUtil.read(req, ClientRequestDto.class);
        JsonUtil.write(resp, HttpServletResponse.SC_OK, clientService.update(dto, id));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = extractId(req).orElseThrow(() -> new IllegalArgumentException("Id is required"));
        clientService.delete(id);
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
