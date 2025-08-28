package com.tuanhiep.banking_service.configuration;

import com.nimbusds.jose.JOSEException;
import com.tuanhiep.banking_service.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

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

    public Jwt decode(String token) throws JwtException {
        try {
            // ✅ Gọi phương thức verifyToken trực tiếp nếu không cần dùng SignedJWT
            jwtService.verifyToken(token, false);
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(accessTokenSignerKey.getBytes(),"HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
