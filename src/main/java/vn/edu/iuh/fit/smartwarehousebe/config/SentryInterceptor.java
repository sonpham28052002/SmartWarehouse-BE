package vn.edu.iuh.fit.smartwarehousebe.config;

import io.sentry.Sentry;
import io.sentry.ITransaction;
import io.sentry.SpanStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SentryInterceptor implements HandlerInterceptor {

  private static final ThreadLocal<ITransaction> currentTransaction = new ThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String method = request.getMethod();
    String path = request.getRequestURI();

    // Set tag
    Sentry.setTag("http.method", method);
    Sentry.setTag("http.route", path);

    // Bắt đầu transaction (cho performance monitoring)
    ITransaction transaction = Sentry.startTransaction(method + " " + path, "http.server");
    Sentry.setTransaction(String.valueOf(transaction));
    currentTransaction.set(transaction);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    ITransaction transaction = currentTransaction.get();
    if (transaction != null) {
      if (ex != null) {
        transaction.setThrowable(ex);
        transaction.setStatus(SpanStatus.INTERNAL_ERROR);
      } else {
        transaction.setStatus(SpanStatus.OK);
      }
      transaction.finish();
      currentTransaction.remove();
    }
  }
}
