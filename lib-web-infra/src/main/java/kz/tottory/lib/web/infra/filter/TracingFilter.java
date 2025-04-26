package kz.tottory.lib.web.infra.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class TracingFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Получить traceId из заголовка, если передан
            String traceId = request.getHeader("X-Trace-Id");
            if (traceId == null || traceId.isEmpty()) {
                // 2. Сгенерировать новый
                traceId = UUID.randomUUID().toString();
            }

            // 3. Положить в MDC, чтобы попасть во все логи
            MDC.put(TRACE_ID, traceId);

            // 4. Прокинуть traceId в ответный заголовок (если нужно)
            response.setHeader("X-Trace-Id", traceId);

            // 5. Продолжить цепочку фильтров
            filterChain.doFilter(request, response);
        } finally {
            // 6. Обязательно очищать MDC после запроса
            MDC.remove(TRACE_ID);
        }
    }
}
