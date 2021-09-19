package com.adobe.prj.api;

import java.util.Map;

import javax.validation.Valid;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adobe.prj.entity.Employee;
import com.adobe.prj.service.EmployeeService;
import com.adobe.prj.util.AuthenticationRequest;
import com.adobe.prj.util.AuthenticationResponse;
import com.adobe.prj.util.EmployeeAuthenticationProvider;
import com.adobe.prj.util.JwtUtil;
import com.adobe.prj.util.UpdatePasswordRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@Api(value = "FrontController", description = "REST APIs related to Employee Entity!!!!")
class FrontController {

	@Autowired
	private EmployeeAuthenticationProvider authenticationProvider;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private EmployeeService employeeService;

	@ApiOperation(value = "authenticate an employee", tags = "createAuthenticationToken")
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid AuthenticationRequest authenticationRequest)
			throws Exception {
		try {

			Authentication auth = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
			final Employee employee = (Employee) auth.getPrincipal();

			final String jwt = jwtTokenUtil.generateToken(employee);

			return ResponseEntity.ok(new AuthenticationResponse(jwt, employee.getId(), employee.getEmail(),
					employee.getFirstName(), employee.getLastName(), employee.getRole(), employee.getIsPasswordTemp()));
		} catch (BadCredentialsException e) {
			throw e;
		}

	}

	@ApiOperation(value = "change password of an employee", tags = "updatePassword")
	@RequestMapping(value = "/change_password", method = RequestMethod.POST)
	public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest)
			throws Exception {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Employee employee = (Employee) auth.getPrincipal();

		
		if (passwordEncryptor.checkPassword(updatePasswordRequest.getOld_password(), employee.getPassword())
				&& updatePasswordRequest.getNew_password().equals(updatePasswordRequest.getConfirm_password())
				&& !updatePasswordRequest.getNew_password().equals(updatePasswordRequest.getOld_password())) {
			employee = employeeService.updatePassword(employee.getId(), updatePasswordRequest.getNew_password());
			String old_jwt = (String) auth.getDetails();
			JwtUtil.blacklist.add(old_jwt);
			final String jwt = jwtTokenUtil.generateToken(employee);
			return ResponseEntity.ok(new AuthenticationResponse(jwt, employee.getId(), employee.getEmail(),
					employee.getFirstName(), employee.getLastName(), employee.getRole(), employee.getIsPasswordTemp()));

		} else {
			throw new BadCredentialsException("Please confirm password and ensure it is not same as old one !");
		}

	}

	@ApiOperation(value = "logout and invalidate JWT token", tags = "logout")
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity<?> logout() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String old_jwt = (String) auth.getDetails();
		JwtUtil.blacklist.add(old_jwt);
		return ResponseEntity.ok("Successfully Logged out !");

	}

	@ApiOperation(value = "authenticate a JWT token", tags = "authenticate")
	@RequestMapping(value = "/authenticate_jwt", method = RequestMethod.GET)
	public ResponseEntity<?> authenticate() {
		return ResponseEntity.ok("Successfull Authentication");

	}
}
