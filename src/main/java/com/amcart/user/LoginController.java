package com.amcart.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("auth")
public class LoginController {

	@Autowired
	RestTemplate restTemplate;

	@Value("${keycloak.client-id}")
	private String clientID;

	@Value("${keycloak.client-secret}")
	private String clientSecret;

	@Value("${keycloak.user-info-uri}")
	private String userInfoURI;

	@Value("${keycloak.token-uri}")
	private String tokenURI;

	@PostMapping(value = "/token/refresh")
	public String refreshToken(String refreshToken) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientID);
		map.add("client_secret", clientSecret);
		map.add("refresh_token", refreshToken);
		map.add("grant_type", "refresh_token");
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
		return restTemplate.postForObject(tokenURI, request, String.class);
	}

	@PostMapping(value = "/token")
	public String token(String code,String redirectURI) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientID);
		map.add("client_secret", clientSecret);
		map.add("code", code);
		map.add("grant_type", "authorization_code");
		map.add("redirect_uri", redirectURI);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, null);
		return restTemplate.postForObject(tokenURI, request, String.class);
	}

	@GetMapping(value = "/userinfo")
	public String userinfo(@RequestHeader("Authorization") String authorization) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("Authorization", authorization);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(null, map);
		return restTemplate.postForObject(userInfoURI, request, String.class);
	}
}
