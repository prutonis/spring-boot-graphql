package io.morosanu.demographql.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {
    public enum TokenType {
        ACCESS, REFRESH
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, length = 2048)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.ACCESS;

    @Column
    private boolean revoked;

    @Column
    private boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
