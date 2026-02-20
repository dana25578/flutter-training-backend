package com.example.app.order;
import com.example.app.product.Product;
import jakarta.persistence.*;
@Entity
@Table(name ="order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional =false,fetch =FetchType.LAZY)
    @JoinColumn(name ="order_id",nullable =false)
    private Order order;
    @ManyToOne(optional=false,fetch =FetchType.LAZY)
    @JoinColumn(name ="product_id",nullable =false)
    private Product product;
    @Column(nullable =false)
    private int quantity;
    @Column(name ="unit_price",nullable =false)
    private double unitPrice;
    public OrderItem(){}
    public Long getId(){return id;}
    public Order getOrder(){return order;}
    public void setOrder(Order order){this.order=order;}
    public Product getProduct(){return product;}
    public void setProduct(Product product){this.product=product;}
    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public double getUnitPrice(){return unitPrice;}
    public void setUnitPrice(double unitPrice){this.unitPrice=unitPrice;}





    
}
