package com.sp.api.common.security;

public class JwtAuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long id;
    private String email;

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public JwtAuthenticationResponse setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public JwtAuthenticationResponse setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public JwtAuthenticationResponse setTokenType(final String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public JwtAuthenticationResponse setId(final long id) {
        this.id = id;
        return this;
    }

    public JwtAuthenticationResponse setEmail(final String email) {
        this.email = email;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JwtAuthenticationResponse)) {
            return false;
        } else {
            JwtAuthenticationResponse other = (JwtAuthenticationResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label63: {
                    Object this$accessToken = this.getAccessToken();
                    Object other$accessToken = other.getAccessToken();
                    if (this$accessToken == null) {
                        if (other$accessToken == null) {
                            break label63;
                        }
                    } else if (this$accessToken.equals(other$accessToken)) {
                        break label63;
                    }

                    return false;
                }

                Object this$refreshToken = this.getRefreshToken();
                Object other$refreshToken = other.getRefreshToken();
                if (this$refreshToken == null) {
                    if (other$refreshToken != null) {
                        return false;
                    }
                } else if (!this$refreshToken.equals(other$refreshToken)) {
                    return false;
                }

                Object this$tokenType = this.getTokenType();
                Object other$tokenType = other.getTokenType();
                if (this$tokenType == null) {
                    if (other$tokenType != null) {
                        return false;
                    }
                } else if (!this$tokenType.equals(other$tokenType)) {
                    return false;
                }

                if (this.getId() != other.getId()) {
                    return false;
                } else {
                    Object this$email = this.getEmail();
                    Object other$email = other.getEmail();
                    if (this$email == null) {
                        if (other$email != null) {
                            return false;
                        }
                    } else if (!this$email.equals(other$email)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    private boolean canEqual(final Object other) {
        return other instanceof JwtAuthenticationResponse;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;

        Object $accessToken = this.getAccessToken();
        result = result * 59 + ($accessToken == null ? 43 : $accessToken.hashCode());
        Object $refreshToken = this.getRefreshToken();
        result = result * 59 + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        Object $tokenType = this.getTokenType();
        result = result * 59 + ($tokenType == null ? 43 : $tokenType.hashCode());
        long $id = this.getId();
        result = result * 59 + (int)($id >>> 32 ^ $id);
        Object $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        return result;
    }
}
