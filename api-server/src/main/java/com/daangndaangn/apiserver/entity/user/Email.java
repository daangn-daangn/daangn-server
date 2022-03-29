package com.daangndaangn.apiserver.entity.user;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;


import static java.util.regex.Pattern.matches;

@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
@Access(AccessType.FIELD)
public class Email {

    @Column(name = "email")
    private String address;

    public String getName() {
        String[] tokens = address.split("@");
        return tokens.length == 2 ? tokens[0] : null;
    }

    public String getDomain() {
        String[] tokens = address.split("@");
        return tokens.length == 2 ? tokens[1] : null;
    }

    public static boolean checkEmailAddress(String address) {
        return matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", address);
    }

    public static Email from(String emailAddress) {
        if (checkEmailAddress(emailAddress)) {
            return new Email(emailAddress);
        }

        throw new RuntimeException("Invalid email address");
    }

}
