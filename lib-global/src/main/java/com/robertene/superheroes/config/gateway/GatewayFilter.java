package com.robertene.superheroes.config.gateway;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import com.robertene.superheroes.common.JsonUtils;
import com.robertene.superheroes.domain.dto.error.ErrorInfo;
import com.robertene.superheroes.exception.AccessDeniedException;
import com.robertene.superheroes.exception.W2MException;

public class GatewayFilter implements Filter {

	private static final String X_FORWARDED_HOST_HEADER = "x-forwarded-host";

	private Boolean isDisabled;
	private List<String> allowedForwards;

	public static final List<String> openApiEndpoints = List.of("/h2-console/**", "/swagger-resources/**",
			"/swagger-ui.html", "/v2/api-docs", "/webjars/**", "/api/auth/session/**");

	public static final Predicate<HttpServletRequest> isSecured = request -> openApiEndpoints.stream()
			.noneMatch(uri -> new AntPathMatcher().match(uri, request.getRequestURI()));

	public GatewayFilter(Boolean isDisabled, List<String> allowedForwards) {
		this.isDisabled = isDisabled;
		this.allowedForwards = allowedForwards;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (!isDisabled.booleanValue() || isSecured.test(req)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			String xForwardedHost = req.getHeader(X_FORWARDED_HOST_HEADER);
			if (xForwardedHost == null || (allowedForwards != null && !allowedForwards.contains(xForwardedHost))) {
				throw new AccessDeniedException();
			}
			chain.doFilter(request, response);
		} catch (W2MException ex) {
			ErrorInfo error = ErrorInfo.of(ex);
			res.setStatus(ex.getHttpStatus());
			res.getWriter().print(JsonUtils.toPrettyJSON(error));
			res.flushBuffer();
		}
	}
}
