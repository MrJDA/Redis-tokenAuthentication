package com.oocl.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TokenUtil {

    private static Key getKeyInstance(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("bankgl");
        Key signKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return signKey;
    }

    public static String createToken(Map<String, Object> claims){
        return Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, getKeyInstance()).compact();
    }

    public static Map<String, Object> parseToken(String token){
        return Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token).getBody();
    }

}

