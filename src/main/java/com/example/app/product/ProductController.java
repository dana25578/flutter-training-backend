package com.example.app.product;
import com.example.app.product.dto.ProductResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins="*")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service){
        this.service=service;
    }
    @GetMapping
    public List<ProductResponse> getAll(){
        return service.getAll();
    }
    @GetMapping("/by-category/{categoryId}")
    public List<ProductResponse> getByCategory(@PathVariable Long categoryId){
        return service.getByCategory(categoryId);
    }
}
