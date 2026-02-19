package com.example.app.product;
import com.example.app.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){
        this.repo=repo;
    }
    private ProductResponse toResponse(Product p){
        ProductResponse r=new ProductResponse();
        r.id=p.getId();
        r.name=p.getName();
        r.description=p.getDescription();
        r.price=p.getPrice();
        r.imageUrl=p.getImageUrl();
        r.categoryId=p.getCategory().getId();
        return r;
    }
    public List<ProductResponse> getAll(){
        return repo.findAll().stream().map(this::toResponse).toList();
    }
    public List<ProductResponse>getByCategory(Long categoryId){
        return repo.findByCategoryId(categoryId).stream().map(this::toResponse).toList();
    }
}
