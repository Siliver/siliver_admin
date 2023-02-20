package com.siliver.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String ISS = "siliversunshine";
    private static final String SECRET_KEY = "siliver";
    private static final String REFRESH_TOKEN_KEY = "siliversunshine";

    // 角色的key
    private static final String ROLE_CLAIMS = "roles";

    private static final String USER_ID = "userId";

    // 过期时间是86400秒，既是1个天
    private static final long EXPIRATION = 86400L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;

    /**
     * 创建token
     *
     * @param username     用户名称
     * @param roles        用户角色
     * @param isRememberMe 是否记住
     * @return 用户TOKEN字符串
     */
    public static String createToken(String username, List<String> roles, int userId, boolean isRememberMe, @Nullable boolean refresh_flag) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        Map<String, Object> map = new HashMap<>(1);
        map.put(ROLE_CLAIMS, roles);
        map.put(USER_ID, userId);
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(formatKey(refresh_flag ? REFRESH_TOKEN_KEY : SECRET_KEY)), SignatureAlgorithm.HS512)
                .setClaims(map)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 验证Token合法性
     *
     * @param token    用户TOKEN
     * @param username 用户名称
     * @return 合法性验证
     */
    public static Boolean verifyToken(String token, String username) {
        boolean isExpiration = isExpiration(token);
        String name = Jwts.parserBuilder().setSigningKey(formatKey(SECRET_KEY)).build().parseClaimsJws(token).getBody().getSubject();
        return (name.equals(username) && !isExpiration);
    }

    /**
     * 验证RefreshToken合法性
     *
     * @param token    用户TOKEN
     * @param username 用户名称
     * @return 合法性结果
     */
    public static Boolean verifyRefreshToken(String token, String username) {
        Claims claims = Jwts.parserBuilder().setSigningKey(formatKey(REFRESH_TOKEN_KEY)).build().parseClaimsJws(token).getBody();
        boolean isExpiration = claims.getExpiration().before(new Date());
        String name = claims.getSubject();
        return (name.equals(username) && !isExpiration);
    }

    /**
     * 从token中获取用户名
     *
     * @param token 用户TOKEN
     * @return token字符串
     */
    public static String getUsername(String token) {
        String userName = null;
        try {
            userName = getTokenBody(token).getSubject();
        } catch (Exception e) {
            log.error("token:" + token + "，解析异常：", e);
        }
        return userName;
    }

    /**
     * 从token中获取用户名
     *
     * @param token 用户TOKEN
     * @return token字符串
     */
    public static String getUsernameByRefresh(String token) {
        String userName = null;
        try {
            userName = getTokenBodyByRefresh(token).getSubject();
        } catch (Exception e) {
            log.error("token:" + token + "，解析异常：", e);
        }
        return userName;
    }

    /**
     * 获取用户角色
     *
     * @param token 用户TOKEN
     * @return token字符串
     */
    public static List<String> getUserRole(String token) {
        Object rolesObject = getTokenBody(token).get(ROLE_CLAIMS);
        return ((List<?>) rolesObject).stream().map(t -> {
            if (t instanceof String temp) {
                return temp;
            }
            return "";
        }).collect(Collectors.toList());
    }

    /**
     * 获取用户角色
     *
     * @param token 用户TOKEN
     * @return token字符串
     */
    public static List<String> getUserRoleByRefresh(String token) {
        Object rolesObject = getTokenBodyByRefresh(token).get(ROLE_CLAIMS);
        return ((List<?>) rolesObject).stream().map(t -> {
            if (t instanceof String temp) {
                return temp;
            }
            return "";
        }).collect(Collectors.toList());
    }

    public static int getUserId(String token) {
        Object userIdObject = getTokenBody(token).get(USER_ID);
        return Integer.parseInt(String.valueOf(userIdObject));
    }

    public static int getUserIdByRefresh(String token) {
        Object userIdObject = getTokenBodyByRefresh(token).get(USER_ID);
        return Integer.parseInt(String.valueOf(userIdObject));
    }

    // 是否已过期
    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parserBuilder().setSigningKey(formatKey(SECRET_KEY)).build().parseClaimsJws(token).getBody();
    }

    private static Claims getTokenBodyByRefresh(String token) {
        return Jwts.parserBuilder().setSigningKey(formatKey(REFRESH_TOKEN_KEY)).build().parseClaimsJws(token).getBody();
    }

    /**
     * 格式化秘钥长度，补全到64位
     */
    private static byte[] formatKey(String secretKey) {
        if (secretKey.length() < 64) {
            //如果key的长度不够，在前面补空格
            secretKey = String.format("%64s", secretKey);
        }
        byte[] bytes;
        bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }
}
