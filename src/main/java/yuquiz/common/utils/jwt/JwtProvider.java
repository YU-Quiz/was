package yuquiz.common.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long accessExpiration;
    private final long refreshExpiration;
    private final String issuer;

    public JwtProvider(@Value("${jwt.secret}") String SECRET_KEY,
                   @Value("${jwt.access-expiration}") long accessExpiration,
                   @Value("${jwt.refresh-expiration}") long refreshExpiration,
                   @Value("${jwt.issuer}") String issuer) {
        this.secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
        this.issuer = issuer;
    }

    /* access Token 발급 */
    public String generateAccessToken(String role, Long userId, String username) {

        return createJwt(createClaims(role, userId), username, accessExpiration);
    }

    /* refresh Token 발급 */
    public String generateRefreshToken(String role, Long userId, String username) {

        return createJwt(createClaims(role, userId), username, refreshExpiration);
    }

    /* claims 생성 */
    private Map<String, Object> createClaims(String role, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);
        return claims;
    }

    /* 토큰 생성 */
    private String createJwt(Map<String, Object> claims, String subject, Long expirationTime) {

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    /* 토큰 정보 불러오기 */
    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /* 토큰(claim)에서 username 가져오기 */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /* 토큰(claim)에서 권한(role) 가져오기 */
    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /* 토큰(claim)에 저장된 category가 refresh, access인지 확인 */
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    /* 토큰에 지정한 만료 시간 확인*/
    public boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }
}