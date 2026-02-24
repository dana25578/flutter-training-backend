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
import com.example.app.email.EmailService;
import org.springframework.beans.factory.annotation.Value;
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final EmailService emailService;
    @Value("${app.publicBaseUrl}")
    private String publicBaseUrl;
    public OrderService(OrderRepository orderRepo,UserRepository userRepo,ProductRepository productRepo,EmailService emailService){
        this.orderRepo=orderRepo;
        this.userRepo=userRepo;
        this.productRepo =productRepo;
        this.emailService = emailService;
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
        try{
            String subject="New Order Placed (Order #"+saved.getId()+")";
            String html=buildOrderEmailHtml(saved,user);
            emailService.sendOwnerNewOrderEmailHtml(subject,html);
        }catch (Exception e){
            System.out.println("Email sending failed: "+e.getMessage());
        }
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
    private String buildOrderEmailHtml(Order saved,User user){
    StringBuilder itemsHtml= new StringBuilder();
    for(OrderItem it:saved.getItems()){
        itemsHtml.append("""
            <tr>
            <td style="padding:10px;border-bottom:1px solid#eee;">
                <div style="font-weight:600;">%s</div>
                <div style="color:#666;font-size:13px;">Qty: %d</div>
            </td>
            <td style="padding:10px;border-bottom:1px solid #eee;text-align:right;">
                <div style="font-weight:600;">$%.2f</div>
                <div style="color:#666;font-size:13px;">Unit: $%.2f</div>
            </td>
            </tr>
        """.formatted(it.getProduct().getName(),it.getQuantity(),(it.getUnitPrice() * it.getQuantity()),it.getUnitPrice()));
    }
    return """
    <div style="font-family:Arial,sans-serif;background:#f6f7fb;padding:20px;">
      <div style="max-width:700px;margin:auto;background:#fff;border-radius:16px; overflow:hidden;box-shadow:0 6px 20px rgba(0,0,0,0.08);">
        <div style="padding:18px 22px;background:#111827;color:#fff;">
          <div style="font-size:18px;font-weight:700;">New Order Placed</div>
          <div style="opacity:0.9;font-size:13px;">Order #%d • %s</div>
        </div>
        <div style="padding:18px 22px;">
          <div style="display:flex;gap:14px;flex-wrap:wrap;">
            <div style="flex:1;min-width:260px;background:#f9fafb;border:1px solid #eee;border-radius:12px;padding:12px;">
              <div style="font-weight:700;margin-bottom:8px;">Customer</div>
              <div><b>Name:</b> %s</div>
              <div><b>Email:</b> %s</div>
              <div><b>Phone:</b> %s</div>
            </div>
            <div style="flex:1;min-width:260px;background:#f9fafb;border:1px solid #eee;border-radius:12px;padding:12px;">
              <div style="font-weight:700;margin-bottom:8px;">Delivery</div>
              <div><b>Address:</b> %s</div>
            </div>
          </div>
          <h3 style="margin:18px 0 10px;">Items</h3>
          <table width="100%%" cellspacing="0" cellpadding="0" style="border-collapse:collapse;">
            %s
          </table>
          <div style="margin-top:16px;padding:14px;background:#f3f4f6;border-radius:12px;display:flex;justify-content:space-between;">
            <div style="font-weight:700;">Total</div>
            <div style="font-weight:800;">$%.2f</div>
          </div>
        </div>
      </div>
    </div>
    """.formatted(saved.getId(),saved.getCreatedAt(),user.getUsername(),user.getEmail(),user.getPhoneNumber(),saved.getAddress(),itemsHtml.toString(),saved.getTotal());
    }
}