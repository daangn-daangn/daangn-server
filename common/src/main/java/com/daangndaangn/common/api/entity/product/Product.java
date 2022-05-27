package com.daangndaangn.common.api.entity.product;

import com.daangndaangn.common.api.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.common.api.entity.category.Category;
import com.daangndaangn.common.api.entity.user.Location;
import com.daangndaangn.common.api.entity.user.User;
import lombok.*;

import javax.persistence.*;

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

    @Column(nullable = false, length = 20)
    private Location location;

    @Column(nullable = false)
    private ProductState state;

    @Column(length = 250)
    private String thumbNailImage;

    @Builder
    private Product(User seller,
                    User buyer,
                    Category category,
                    String name,
                    long price,
                    String title,
                    String description,
                    ProductState state) {

        this.seller = seller;
        this.buyer = buyer;
        this.category = category;
        this.name = name;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = seller.getLocation();
        this.state = state;
    }

    public void updateInfo(String title, String name, Category category, Long price, String description) {
        this.title = title;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public void updateState(ProductState state) {
        this.state = state;
    }
}
