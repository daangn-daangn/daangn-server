package com.daangndaangn.apiserver.entity.user;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 사용자의 위치 정보를 나타내는 Value Object
 */
@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
@Access(AccessType.FIELD)
public class Location {

    @Column(name = "location")
    private String address;

    public static Location from(String address) {
        return new Location(address);
    }

}
