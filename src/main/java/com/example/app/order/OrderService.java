package com.example.app.order;
import com.example.app.order.dto.CreateOrderItemRequest;
import com.example.app.order.dto.CreateOrderRequest;
import com.example.app.order.dto.OrderItemResponse;
import com.example.app.order.dto.OrderResponse;
import com.example.app.product.Product;
import com.example.app.product.ProductRepository;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    public OrderService(OrderRepository orderRepo,UserRepository userRepo,ProductRepository productRepo){
        this.orderRepo=orderRepo;
        this.userRepo=userRepo;
        this.productRepo =productRepo;
    }
    private OrderResponse toResponse(Order order){
        OrderResponse r=new OrderResponse();
        r.id=order.getId();
        r.userId=order.getUser().getId();
        r.address =order.getAddress();
        r.total=order.getTotal();
        r.createdAt= order.getCreatedAt();
        List<OrderItemResponse> items=new ArrayList<>();
        for (OrderItem it:order.getItems()){
            OrderItemResponse ir=new OrderItemResponse();
            ir.productId=it.getProduct().getId();
            ir.productName=it.getProduct().getName();
            ir.imageUrl=it.getProduct().getImageUrl();
            ir.quantity=it.getQuantity();
            ir.unitPrice=it.getUnitPrice();
            items.add(ir);
        }
        r.items=items;
        return r;
    }
    @Transactional
    public OrderResponse create(CreateOrderRequest req){
        if (req==null) throw new RuntimeException("request is missing");
        if (req.userId==null) throw new RuntimeException("userId is required");
        if (req.items==null||req.items.isEmpty()) throw new RuntimeException("items are required");
        String address=(req.address==null)?"":req.address.trim();
        if (address.isEmpty()) throw new RuntimeException("address is required");
        Optional<User> userOpt=userRepo.findById(req.userId);
        if (userOpt.isEmpty()) throw new RuntimeException("user not found");
        User user=userOpt.get();
        Order order=new Order();
        order.setUser(user);
        order.setAddress(address);
        double total=0;
        for (CreateOrderItemRequest itemReq:req.items){
            if(itemReq.productId==null) throw new RuntimeException("productId is required");
            if (itemReq.quantity<=0) throw new RuntimeException("quantity must be greater than 0");
            Optional<Product> productOpt =productRepo.findById(itemReq.productId);
            if (productOpt.isEmpty()) throw new RuntimeException("product not found: "+itemReq.productId);
            Product product=productOpt.get();
            OrderItem orderItem=new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.quantity);
            orderItem.setUnitPrice(product.getPrice());
            order.getItems().add(orderItem);
            total=total+(product.getPrice()*itemReq.quantity);
        }
        order.setTotal(total);
        Order saved=orderRepo.save(order);
        return toResponse(saved);
    }
    public List<OrderResponse> getByUser(Long userId){
        if (userId==null) throw new RuntimeException("userId is required");
        List<Order> orders=orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderResponse> responses=new ArrayList<>();
        for (Order o:orders) {
            responses.add(toResponse(o));
        }
        return responses;
    }
}