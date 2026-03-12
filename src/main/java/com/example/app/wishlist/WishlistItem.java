package com.example.app.wishlist;
import com.example.app.product.Product;
import com.example.app.user.User;
import jakarta.persistence.*;
@Entity
@Table(name="wishlist_items",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","product_id"}))
public class WishlistItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private User user;
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="product_id",nullable=false)
    private Product product;
    public WishlistItem(){}
    public Long getId(){
        return id;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }

    public Product getProduct(){
        return product;
    }
    public void setProduct(Product product){
        this.product=product;
    }
}
