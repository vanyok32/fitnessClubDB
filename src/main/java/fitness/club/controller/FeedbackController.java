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
        // Проверяем query параметр clientId
        String clientIdParam = req.getParameter("clientId");
        if (clientIdParam != null && !clientIdParam.isBlank()) {
            try {
                Integer clientId = Integer.parseInt(clientIdParam);
                // Вызываем метод сервиса для получения отзывов по clientId
                // false означает, что мы ищем по client_id, а не по coach_id
                JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findByClientOrCoachId(clientId, false));
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid clientId parameter");
            }
            return; // Важно: выйти из метода, чтобы не выполнить остальную логику
        }

        // Если clientId не указан, проверяем путь (scheduleId)
        Optional<Integer> scheduleId = extractId(req);
        if (scheduleId.isPresent()) {
            // Вызываем метод сервиса для получения отзыва по scheduleId (если он используется где-то еще)
            // ВНИМАНИЕ: findByScheduleId в сервисе возвращает ОДИН объект, а не список
            // Если вы вызываете этот путь, ожидайте один отзыв.
            // JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findByScheduleId(scheduleId.get()));
            // Однако, ваш репозиторий findByClientOrCoachId возвращает List.
            // Лучше не использовать этот путь, если вы не хотите возвращать список для одного ID.
            // Или измените сервис, чтобы findByScheduleId тоже возвращал List.
            // Для простоты, пусть будет так, но имейте в виду:
            JsonUtil.write(resp, HttpServletResponse.SC_OK, service.findByScheduleId(scheduleId.get()));
        } else {
            // Логика для получения всех отзывов (если не указан ни clientId, ни scheduleId в пути)
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
