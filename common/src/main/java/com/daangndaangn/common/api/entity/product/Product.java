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

    @Column(nullable = false)
    private int viewCnt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages = new ArrayList<>();

    @Builder
    private Product(Long id,
                    User seller,
                    Category category,
                    long price,
                    String title,
                    String description) {

        checkArgument(seller != null, "seller ?????? ???????????????.");
        checkArgument(category != null, "category ?????? ???????????????.");
        checkArgument(isNotEmpty(title), "title ?????? ???????????????.");
        checkArgument(isNotEmpty(description), "description ?????? ???????????????.");
        checkArgument(
                seller.getLocation() != null && isNotEmpty(seller.getLocation().getAddress()),
                "???????????? ?????? ????????? ???????????????.");
        checkArgument(title.length() <= 100, "????????? ????????? 100??? ???????????? ?????????.");
        checkArgument(description.length() <= 100, "????????? ????????? 100??? ???????????? ?????????.");

        this.id = id;
        this.seller = seller;
        this.buyer = null;
        this.category = category;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = seller.getLocation();
        this.productState = ProductState.FOR_SALE;
        this.refreshCnt = 0;
        this.viewCnt = 0;
    }

    public void setThumbnailImage(String thumbNailImage) {
        checkArgument(isNotEmpty(thumbNailImage), "thumbNailImage ?????? ???????????????.");
        checkArgument(thumbNailImage.length() <= 250, "thumbNailImage ?????? 250??? ???????????? ?????????.");

        this.thumbNailImage = thumbNailImage;
    }

    // ???????????? ?????? ?????????: ????????? ?????? ????????? ????????? ???????????? ??? ??????????????? ???????????? ?????????
    public void addProductImage(String productImageUrl) {
        productImages.add(ProductImage.of(this, productImageUrl));
    }

    public void updateInfo(String title, Category category, Long price, String description) {
        checkArgument(
                isEmpty(title) || title.length() <= 100,
                "????????? ????????? 100??? ???????????? ?????????.");
        checkArgument(
                isEmpty(description) || description.length() <= 100,
                "?????? ????????? 100??? ???????????? ?????????.");

        this.title = isEmpty(title) ? this.title : title;
        this.category = category == null ? this.category : category;
        this.price = price == null ? this.price : price;
        this.description = isEmpty(description) ? this.description : description;
    }

    public void updateState(ProductState state) {
        this.productState = state;
    }

    public void updateBuyer(User buyer) {
        checkArgument(buyer != null, "buyer ?????? ???????????????.");
        this.buyer = buyer;
    }

    public void increaseViewCount(int viewCount) {
        this.viewCnt += viewCount;
    }

    public void refresh() {
        checkState(refreshCnt + 1 <= 5, "?????? ????????? 5????????? ??? ??? ????????????.");
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
