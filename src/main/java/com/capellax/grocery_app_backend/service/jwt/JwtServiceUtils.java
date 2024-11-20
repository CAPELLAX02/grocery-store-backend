package com.capellax.grocery_app_backend.service.jwt;

import com.capellax.grocery_app_backend.exception.custom.CustomRuntimeException;
import com.capellax.grocery_app_backend.exception.enums.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtServiceUtils {

//    @Value("${security.jwt.secret}")
    private final String jwtSecret = "KDskFIEOEDUnoobSrqCfCGTcSvsTCGDujUXNDVS+IKvkbBBDLYgDaEYs5avdNF6AIUtbW1OorIYXMWeJrrNrMm4X68ELdQBwlWRq8RTwnKud0AQaVCjTQBfjmGmX2rkKaXz7SMAuUuGwgsX32tfQvWKmO/cwSwjjtyj3EqE/2BN8";

//    private final JwtConfig jwtConfig;

    protected Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            throw new CustomRuntimeException(ErrorCode.INVALID_OR_EXPIRED_ACCESS_TOKEN);
        }
    }

}
