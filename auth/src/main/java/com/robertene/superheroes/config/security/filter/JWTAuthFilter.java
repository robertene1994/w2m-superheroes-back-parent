package com.robertene.superheroes.config.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.robertene.superheroes.common.CurrentThreadService;
import com.robertene.superheroes.common.JsonUtils;
import com.robertene.superheroes.config.security.util.TokenHelper;
import com.robertene.superheroes.domain.dto.error.ErrorInfo;
import com.robertene.superheroes.domain.dto.session.SessionData;
import com.robertene.superheroes.domain.exception.InvalidTokenException;
import com.robertene.superheroes.exception.W2MException;

public class JWTAuthFilter extends OncePerRequestFilter {

	private static final String AUTH_HEADER = "Authorization";
	private static final String TOKEN_PARAM = "_token";
	private static final String ROLE_PREFIX = "ROLE_";

	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthFilter.class);

	private TokenHelper tokenHelper;
	private CurrentThreadService currentThreadService;

	public JWTAuthFilter(TokenHelper tokenHelper, CurrentThreadService currentThreadService) {
		this.tokenHelper = tokenHelper;
		this.currentThreadService = currentThreadService;
	}

	public SessionData extractTokenData(HttpServletRequest request) throws W2MException {
		String authHeaderVal = request.getHeader(AUTH_HEADER);
		String token;
		if (authHeaderVal == null) {
			token = request.getParameter(TOKEN_PARAM);
			if (token == null) {
				return null;
			}
		} else {
			if (!authHeaderVal.startsWith("Bearer ")) {
				throw new InvalidTokenException();
			}
			token = authHeaderVal.substring("Bearer ".length()).trim();
		}

		SessionData userData = tokenHelper.getSessionData(token);
		if (userData == null) {
			throw new InvalidTokenException();
		}
		return userData;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			SessionData sessionData = extractTokenData(request);
			if (sessionData != null) {
				LOG.info("Session data: {}", sessionData);
				List<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + sessionData.getRole()));
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						sessionData.getUsername(), null, authorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
				currentThreadService.set(SessionData.KEY, sessionData);
			}
			filterChain.doFilter(request, response);

		} catch (W2MException ex) {
			SecurityContextHolder.clearContext();
			ErrorInfo error = ErrorInfo.of(ex);
			response.setStatus(ex.getHttpStatus());
			response.getWriter().print(JsonUtils.toPrettyJSON(error));
			response.flushBuffer();
		} catch (Exception ex) {
			SecurityContextHolder.clearContext();
			ErrorInfo error = ErrorInfo.of(HttpStatus.I_AM_A_TEAPOT.value(), ex.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().print(JsonUtils.toPrettyJSON(error));
			response.flushBuffer();
		}
	}
}
