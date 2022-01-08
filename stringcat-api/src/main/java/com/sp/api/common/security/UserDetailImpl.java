package com.sp.api.common.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.api.common.exception.ApiException;
import com.sp.domain.code.UserRole;
import com.sp.domain.domain.user.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailImpl implements UserDetails {

    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private String nickname;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailImpl create(Users user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(Objects.isNull(user)) throw new ApiException("Failed to log in");

        //default -> ROLE_GUEST
        authorities.add(new SimpleGrantedAuthority("ROLE_GUEST"));

        //로그인 성공시 -> ROLE_USER
        if(UserRole.USER.equals(user.getRole())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new UserDetailImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailImpl that = (UserDetailImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
