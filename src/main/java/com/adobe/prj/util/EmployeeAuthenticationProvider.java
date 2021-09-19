package com.adobe.prj.util;

import java.util.ArrayList;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.adobe.prj.entity.Employee;
import com.adobe.prj.exceptions.NotFoundException;
import com.adobe.prj.service.EmployeeService;

@Component
public class EmployeeAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private EmployeeService employeeService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		String username = authentication.getPrincipal() + "";
		String password = authentication.getCredentials() + "";

		Employee employee;
		try {
			employee = employeeService.getEmployeeByEmail(username);
		} catch (NotFoundException e) {
			throw new BadCredentialsException("Email doesn't exists !!");
		}

		if (!passwordEncryptor.checkPassword(password, employee.getPassword())) {
			throw new BadCredentialsException("Password incorrect !!");
		}

		return new UsernamePasswordAuthenticationToken(employee, null, new ArrayList<>());

	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}
}
