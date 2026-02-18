package com.example.app.category;
import com.example.app.category.dto.CategoryResponse;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CategoryService {
    private final CategoryRepository repo;
    public CategoryService(CategoryRepository repo){
        this.repo=repo;
    }
    private CategoryResponse toResponse(Category c){
        CategoryResponse r=new CategoryResponse();
        r.id=c.getId();
        r.name=c.getName();
        r.imageUrl=c.getImageUrl();
        return r;
    }
    public List<CategoryResponse> getAll(){
        return repo.findAll().stream().map(this::toResponse).toList();
    }
}
