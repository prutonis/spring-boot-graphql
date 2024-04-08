package io.morosanu.demographql.sec;

import io.morosanu.demographql.domain.User;
import jakarta.annotation.PostConstruct;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private RsaJsonWebKey rsaJsonWebKey;
    @PostConstruct
    private void createRsaJsonWebKey() throws JoseException {
        rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId("k1");
    }
    public <X> X getTokenClaim(String jwt, Function<JwtClaims, X> claimExtractor) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setExpectedAudience("demo")
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .build();
        JwtClaims jwtClaims = null;
        try {
            jwtClaims = jwtConsumer.processToClaims(jwt);
        } catch (InvalidJwtException e) {
            throw new RuntimeException(e);
        }
        return claimExtractor.apply(jwtClaims);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setExpectedAudience("demo")
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .setRequireExpirationTime()
                .build();
        try {
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            if (!jwtClaims.getSubject().equals(userDetails.getUsername())) {
                return false;
            }
        } catch (InvalidJwtException | MalformedClaimException e) {
            return false;
        }
        return true;
    }

    public String generateToken(@NonNull Map<String, Object> additionalClaims, User user, long expiration) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setSubject(user.getEmail());
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setAudience("demo");
        jwtClaims.setExpirationTimeMinutesInTheFuture(expiration);
        jwtClaims.setGeneratedJwtId();
        jwtClaims.setClaim("role", user.getRole().name());
        jwtClaims.setClaim("name", user.getName());
        jwtClaims.setClaim("email", user.getEmail());
        jwtClaims.setIssuer("demo-app");
        additionalClaims.forEach(jwtClaims::setClaim);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
