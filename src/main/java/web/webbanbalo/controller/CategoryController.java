package web.webbanbalo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.webbanbalo.entity.Category;
import web.webbanbalo.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CategoryController {

    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/category")
    public ResponseEntity<String> createCategory(@RequestBody Category category){

        categoryRepository.save(category);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category")
    public List<Category> getListCategory(){
        return categoryRepository.findAll();
    }

    @GetMapping("/category/{id}")
    public Category getCategoryById(@PathVariable int id){
        Optional<Category> c = categoryRepository.findById(id);

        return c.get();
    }


    @PutMapping("/category/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody Category updatedCategory) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category existingCategory = optionalCategory.get();

        existingCategory.setNameC(updatedCategory.getNameC());

        categoryRepository.save(existingCategory);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Category categoryToDelete = optionalCategory.get();
        categoryRepository.delete(categoryToDelete);

        return ResponseEntity.noContent().build();
    }

}
