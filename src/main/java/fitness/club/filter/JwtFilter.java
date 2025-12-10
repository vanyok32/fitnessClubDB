package fitness.club.filter;

import fitness.club.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*", "/profile"})
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                Claims claims = JwtUtil.validate(auth.substring(7));
                request.setAttribute("userId", Long.parseLong(claims.getSubject()));
                request.setAttribute("userName", claims.get("name", String.class));
                request.setAttribute("userType", claims.get("type", String.class));
                chain.doFilter(req, resp);
                return;
            } catch (Exception e) {
                // токен невалидный
            }
        }
        ((HttpServletResponse) resp).sendError(401, "Требуется вход");
    }
}