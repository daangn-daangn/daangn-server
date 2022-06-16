package com.daangndaangn.common.api.entity.user;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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

    @Column(name = "location", length = 20)
    private String address;

    public static Location from(String address) {
        checkArgument(isNotEmpty(address), "address 값은 필수입니다.");
        checkArgument(address.length() <= 20, "주소는 20자 이하여야 합니다.");

        return new Location(address);
    }

}
