package com.sp.api.common.security;

import com.sp.api.common.utils.Sha256;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Sha256PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return Sha256.encode(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        if (charSequence == null) return false;

        return encode(charSequence).equals(s);
    }
}

