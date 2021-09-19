package com.adobe.prj.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.adobe.prj.entity.Employee;
import com.adobe.prj.service.EmployeeService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private final HandlerExceptionResolver handlerExceptionResolver;

	public JwtRequestFilter(HandlerExceptionResolver handlerExceptionResolver) {
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException, JwtException {

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		Employee employee;

		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				username = jwtUtil.extractUsername(jwt);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				employee = this.employeeService.getEmployeeByEmail(username);
				if (jwtUtil.validateToken(jwt, employee)) {
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(employee, null,
							new ArrayList<>());
					token.setDetails(jwt);
					SecurityContextHolder.getContext().setAuthentication(token);
				}
			}
			chain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			handlerExceptionResolver.resolveException(request, response, null, e);
			throw new JwtException(e.getMessage());
		} catch (SignatureException e) {
			handlerExceptionResolver.resolveException(request, response, null, e);
//			throw new JwtException(e.getMessage());
		} catch (Exception e) {
			handlerExceptionResolver.resolveException(request, response, null, e);
//			throw new JwtException(e.getMessage());
		}
	}

}
