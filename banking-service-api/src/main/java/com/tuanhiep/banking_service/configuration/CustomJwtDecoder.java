package com.tuanhiep.banking_service.configuration;

import com.nimbusds.jose.JOSEException;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.ACCESS_TOKEN_SIGNER_KEY}")
    protected String accessTokenSignerKey;

    private final JwtService jwtService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    public CustomJwtDecoder(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            jwtService.verifyToken(token, false);
        } catch (AppException ex) {
            // Gắn AppException vào request
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                attributes.getRequest().setAttribute("exception", ex);
            }
            throw new JwtException(ex.getMessage(), ex);
        } catch (JOSEException | ParseException e) {
            // Các lỗi parse/verify từ thư viện JOSE
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                attributes.getRequest().setAttribute("exception", e);
            }
            throw new JwtException(e.getMessage(), e);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(accessTokenSignerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }}

