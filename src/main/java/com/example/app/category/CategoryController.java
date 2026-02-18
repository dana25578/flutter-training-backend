package com.example.app.category;
import com.example.app.category.dto.CategoryResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins ="*")
public class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service){
        this.service=service;
    }
    @GetMapping
    public List<CategoryResponse> getAll(){
        return service.getAll();
    }
}
