package com.daangndaangn.common.api.entity.product;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
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
    private ProductState productState;

    @Column(length = 250)
    private String thumbNailImage;

    @Column(nullable = false)
    private int refreshCnt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages = new ArrayList<>();

    @Builder
    private Product(Long id,
                    User seller,
                    Category category,
                    String name,
                    long price,
                    String title,
                    String description) {

        checkArgument(seller != null, "seller 값은 필수입니다.");
        checkArgument(category != null, "category 값은 필수입니다.");
        checkArgument(isNotEmpty(name), "name 값은 필수입니다.");
        checkArgument(isNotEmpty(title), "title 값은 필수입니다.");
        checkArgument(isNotEmpty(description), "description 값은 필수입니다.");
        checkArgument(
                seller.getLocation() != null && isNotEmpty(seller.getLocation().getAddress()),
                "판매자의 주소 정보는 필수입니다.");
        checkArgument(name.length() <= 50, "물품명은 50자 이하여야 합니다.");
        checkArgument(title.length() <= 100, "판매글 제목은 100자 이하여야 합니다.");
        checkArgument(description.length() <= 100, "판매글 내용은 100자 이하여야 합니다.");

        this.id = id;
        this.seller = seller;
        this.buyer = null;
        this.category = category;
        this.name = name;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = seller.getLocation();
        this.productState = ProductState.FOR_SALE;
        this.refreshCnt = 0;
    }

    public void setThumbnailImage(String thumbNailImage) {
        checkArgument(isNotEmpty(thumbNailImage), "thumbNailImage 값은 필수입니다.");
        checkArgument(thumbNailImage.length() <= 250, "thumbNailImage 값은 250자 이하여야 합니다.");

        this.thumbNailImage = thumbNailImage;
    }

    // 연관관계 편의 메서드: 양방향 매핑 관계시 양쪽에 셋팅하는 걸 원자적으로 묶어주는 메서드
    public void addProductImage(String productImageUrl) {
        productImages.add(ProductImage.of(this, productImageUrl));
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
        this.category = category == null ? this.category : category;
        this.price = price == null ? this.price : price;
        this.description = isEmpty(description) ? this.description : description;
    }

    public void updateState(ProductState state) {
        this.productState = state;
    }

    public void updateBuyer(User buyer) {
        checkArgument(buyer != null, "buyer 값은 필수입니다.");
        this.buyer = buyer;
    }

    public void refresh() {
        checkState(refreshCnt + 1 <= 5, "새로 고침은 5회이상 할 수 없습니다.");
        refreshCnt++;
    }

    public boolean isSeller(Long userId) {
        if (userId == null) {
            return false;
        }

        return seller != null && userId.equals(seller.getId());
    }

    public boolean isBuyer(Long userId) {
        if (userId == null) {
            return false;
        }

        return buyer != null && userId.equals(buyer.getId());
    }
}
