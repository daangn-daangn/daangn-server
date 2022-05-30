package com.daangndaangn.common.api.entity.product;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import lombok.*;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "products")
public class Product extends AuditingCreateUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false)
    private Location location;

    @Column(nullable = false)
    private ProductState state;

    @Column(length = 250)
    private String thumbNailImage;

    @Builder
    private Product(User seller,
                    Category category,
                    String name,
                    long price,
                    String title,
                    String description) {

        checkArgument(seller != null, "seller must not be null");
        checkArgument(category != null, "category must not be null");
        checkArgument(isNotEmpty(name), "name must not be null");
        checkArgument(isNotEmpty(title), "title must not be null");
        checkArgument(isNotEmpty(description), "description must not be null");
        checkArgument(
                isNotEmpty(seller.getLocation()) && isNotEmpty(seller.getLocation().getAddress()),
                "판매자의 주소 정보는 필수입니다.");
        checkArgument(name.length() <= 50, "물품명은 50자 이하여야 합니다.");
        checkArgument(title.length() <= 100, "판매글 제목은 100자 이하여야 합니다.");
        checkArgument(description.length() <= 100, "판매글 내용은 100자 이하여야 합니다.");

        this.seller = seller;
        this.buyer = null;
        this.category = category;
        this.name = name;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = seller.getLocation();
        this.state = ProductState.FOR_SALE;
    }

    public void updateInfo(String title, String name, Category category, Long price, String description) {
        checkArgument(
                isEmpty(title) || title.length() <= 100,
                "판매글 제목은 100자 이하여야 합니다.");
        checkArgument(
                isEmpty(name) || name.length() <= 50,
                "물품명은 50자 이하여야 합니다.");
        checkArgument(
                isEmpty(description) || description.length() <= 100,
                "물품 설명은 100자 이하여야 합니다.");

        this.title = isEmpty(title) ? this.title : title;
        this.name = isEmpty(name) ? this.name : name;
        this.category = isEmpty(category) ? this.category : category;
        this.price = isEmpty(price) ? this.price : price;
        this.description = isEmpty(description) ? this.description : description;
    }

    public void updateState(ProductState state) {
        this.state = state;
    }

    public void updateBuyer(User buyer) {
        checkArgument(buyer != null, "buyer must not be null");
        this.buyer = buyer;
    }
}
