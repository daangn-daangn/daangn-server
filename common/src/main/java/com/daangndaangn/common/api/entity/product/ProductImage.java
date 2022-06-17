package com.daangndaangn.common.api.entity.product;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_images")
public class ProductImage extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, length = 250)
    private String imageUrl;

    public static ProductImage of(Product product, String imageUrl) {
        return new ProductImage(product, imageUrl);
    }

    private ProductImage(Product product, String imageUrl) {
        checkArgument(product != null, "product 값은 필수입니다.");
        checkArgument(isNotEmpty(imageUrl), "imageUrl 값은 필수입니다.");
        checkArgument(imageUrl.length() <= 250, "imageUrl 값은 250자 이하여야 합니다.");

        this.product = product;
        this.imageUrl = imageUrl;
    }
}
