package br.com.sistema.dto.security;

import java.util.Date;

public record TokenResponseDto(String username, Boolean authenticated, Date created, Date expiration, String accessToken) {}
