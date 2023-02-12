package com.marindulja.template.springresttemplate.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApplicationRoles {
    ADMIN(Identifier.ADMIN),
    USER(Identifier.USER);

    private String name;

    public class Identifier {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }

}
