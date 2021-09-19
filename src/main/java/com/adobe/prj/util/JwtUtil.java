package com.adobe.prj.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.adobe.prj.entity.Employee;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	private String SECRET_KEY = "secret";

	public static List<String> blacklist = new ArrayList<String>();
	public static Map<String, String> valid_tokens = new HashMap<String, String>();
	private static Date start_date;

	public static void setStartDate(Date date) {
		start_date = date;
	}

	public String extractUsername(String token) throws Exception {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public Date extractCreation(String token) {
		return extractClaim(token, Claims::getIssuedAt);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		System.out.println(extractCreation(token));
		System.out.println(start_date);
		return extractExpiration(token).before(new Date()) || extractCreation(token).before(start_date);
	}

	public String generateToken(Employee employee) {
		if (valid_tokens.containsKey(employee.getEmail()) && !isTokenExpired(valid_tokens.get(employee.getEmail()))
				&& !blacklist.contains(valid_tokens.get(employee.getEmail()))) {
			return valid_tokens.get(employee.getEmail());
		}
		Map<String, Object> claims = new HashMap<>();
		String token = createToken(claims, employee.getEmail());
		valid_tokens.put(employee.getEmail(), token);
		return token;
	}

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Boolean validateToken(String token, Employee employee) throws Exception {
		final String username = extractUsername(token);
		return (username.equals(employee.getEmail()) && !isTokenExpired(token) && !blacklist.contains(token));
	}
}
