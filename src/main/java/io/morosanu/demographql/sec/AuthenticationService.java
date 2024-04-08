package io.morosanu.demographql.sec;

import io.morosanu.demographql.domain.Token;
import io.morosanu.demographql.domain.User;
import io.morosanu.demographql.repo.TokenRepository;
import io.morosanu.demographql.repo.UserRepository;
import jakarta.annotation.PostConstruct;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<User> usr = userRepository.findByEmail(request.getEmail());
        if (usr.isPresent()) {
            throw new IllegalStateException("User already exists");
        }

        User dbUser = new User();
        dbUser.setEmail(request.getEmail());
        dbUser.setName(request.getName());
        dbUser.setPassword(passwordEncoder.encode(request.getPassword()));
        dbUser.setRole(request.getRole());
        userRepository.save(dbUser);
        AuthenticationResponse response = generateToken(dbUser);
        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
        if (dbUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), dbUser.get().getPassword())) {
            throw new IllegalStateException("Invalid password");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        //invalidate all tokens
        tokenRepository.deleteAllByUser(dbUser.get());

        AuthenticationResponse response = generateToken(dbUser.get());
        return response;
    }

    private AuthenticationResponse generateToken(User user) {
        String authToken = jwtService.generateToken(Map.of(), user, 24*60);
        String refreshToken = jwtService.generateToken(Map.of(), user, 7*24*60);
        Token atoken = new Token();
        atoken.setUser(user);
        atoken.setToken(authToken);
        atoken.setTokenType(Token.TokenType.ACCESS);
        tokenRepository.save(atoken);
        Token rtoken = new Token();
        rtoken.setUser(user);
        rtoken.setToken(refreshToken);
        rtoken.setTokenType(Token.TokenType.REFRESH);
        tokenRepository.save(rtoken);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthToken(authToken);
        response.setRefreshToken(refreshToken);
        return response;
    }
}
