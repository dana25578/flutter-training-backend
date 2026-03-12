package com.example.app.cart;
import com.example.app.product.Product;
import com.example.app.user.User;
import jakarta.persistence.*;
@Entity
@Table(name="cart_items",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","product_id"}))
public class CartItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private User user;
    @ManyToOne(optional=false,fetch=FetchType.LAZY)
    @JoinColumn(name="product_id",nullable=false)
    private Product product;
    @Column(nullable=false)
    private int quantity;
    public CartItem(){}
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
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
}