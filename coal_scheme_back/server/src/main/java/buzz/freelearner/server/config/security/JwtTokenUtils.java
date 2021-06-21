package buzz.freelearner.server.config.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 自动生成token的工具类
 * @author eva
 * @create 2021/4/24
 */
@Component
public class JwtTokenUtils {
    //username
    private static final String CLAIM_KEY_USERNAME = "sub";
    //created time
    private static final String CLAIM_KEY_CREATED = "created";

    //get paras from application.yml
    @Value("${jwt.secret}")
    //secret key
    private String secret;
    //一定不要把名字拼错！！！
    @Value("${jwt.expiration}")
    //how long to be valid
    private Long expiration;

    /**
     * 1. generate token based on user information
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 2. get username from token
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            System.out.println("用户名不存在！");
        }
        return username;
    }

    /**
     * get claims from token
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("获取token荷载失败!");
        }
        return claims;
    }

    /**
     * generate token based on claims
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * generate token Expiration-time
     * @return
     */
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 3. Verify that the token is valid or not
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isValidToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken(token);
        //1. is token out-of-date
        //2. is user info in claims correspond to that in userDetails
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * is Token Expired
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token){
        //get expire date
        Date expireDate = getExpireDateFromToken(token);
        //date > ddl, then expired
        return expireDate.before(new Date());
    }

    /**
     * get token expired time
     * @param token
     * @return
     */
    private Date getExpireDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 4. can Token be refreshed
     * @param token
     * @return boolean
     */
    public boolean canRefreshToken(String token){
        return !isTokenExpired(token);
    }

    /**
     * 5. refresh expired token
     * @param token
     * @return String token
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}

/*
* 共5个可被调用的API*/