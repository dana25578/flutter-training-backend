package com.example.app.product;
import com.example.app.category.Category;
import jakarta.persistence.*;
@Entity
@Table(name ="products")
public class Product {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(nullable =false,length =120)
    private String name;
    @Column(length =255)
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(name ="image_url",length=255)
    private String imageUrl;
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="category_id",nullable=false)
    private Category category;
    public Product(){}
    public Long getId(){return id;}
    public String getName(){return name;}
    public void setName(){this.name=name;}
    public String getDescription(){return description;}
    public void setDescription(){this.description=description;}
    public double getPrice(){return price;}
    public void setPrice(double price){this.price=price;}
    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
    public Category getCategory(){return category;}
    public void setCategory(Category category){this.category=category;}
}
