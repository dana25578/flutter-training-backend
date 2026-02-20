package com.example.app.order;
import com.example.app.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name ="orders")
public class Order {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional =false,fetch =FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable =false)
    private User user;
    @Column(length=255,nullable =false)
    private String address;
    @Column(nullable =false)
    private double total;
    @Column(name ="created_at",nullable =false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy ="order",cascade=CascadeType.ALL,orphanRemoval =true)
    private List<OrderItem> items =new ArrayList<>();
    public Order(){}
    @PrePersist
    protected void onCreate(){
        if(createdAt==null) createdAt=LocalDateTime.now();
    }
    public Long getId(){return id;}
    public User getUser(){return user;}
    public void setUser(User user){this.user=user;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}
    public double getTotal(){return total;}
    public void setTotal(double total){this.total=total;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public List<OrderItem> getItems(){return items;}
    public void setItems(List<OrderItem>items){this.items=items;}
}
