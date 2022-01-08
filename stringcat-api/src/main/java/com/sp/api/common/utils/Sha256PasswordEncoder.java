package com.sp.api.common.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha256PasswordEncoder implements PasswordEncoder {

    public Sha256PasswordEncoder() {

    }

    public String encode(CharSequence charSequence) {
        return Sha256.encode(charSequence.toString());
    }

    public boolean matches(CharSequence charSequence, String str) {
        return charSequence == null ? false : this.encode(charSequence).equals(str);
    }
}
