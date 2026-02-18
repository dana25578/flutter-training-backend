package com.example.app.category;
import jakarta.persistence.*;
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(nullable =false,unique =true,length =60)
    private String name;
    @Column(name="image_url",length =255)
    private String imageUrl;
    public Category(){}
    public Long getId(){return id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
}
    
