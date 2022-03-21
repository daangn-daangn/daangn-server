package com.daangndaangn.apiserver.entity.product;

import com.daangndaangn.apiserver.entity.AuditingCreateUpdateEntity;
import com.daangndaangn.apiserver.entity.category.Category;
import com.daangndaangn.apiserver.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    //TODO: 현재는 사용하지 않을 필드인데 String null로 놔둘지 사용자 정의타입으로 놔둘지, 그냥 삭제해둘지 결정 필요
    @Column(length = 10)
    private String sellingType;

    @Column(nullable = false, length = 20)
    private String location;

    @Column(nullable = false)
    private ProductState state;

    @Column(length = 500)
    private String thumbNailImage;

    @Builder
    private Product(User seller,
                    User buyer,
                    Category category,
                    String name,
                    long price,
                    String title,
                    String description,
                    ProductState state,
                    String thumbNailImage) {

        this.seller = seller;
        this.buyer = buyer;
        this.category = category;
        this.name = name;
        this.price = price;
        this.title = title;
        this.description = description;
        this.location = seller.getLocation();
        this.state = state;
        this.thumbNailImage = thumbNailImage;
    }
}
