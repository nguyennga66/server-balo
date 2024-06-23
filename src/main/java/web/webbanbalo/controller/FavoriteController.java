package web.webbanbalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.webbanbalo.entity.Favorite;
import web.webbanbalo.entity.User;
import web.webbanbalo.entity.Product;
import web.webbanbalo.repository.FavoriteRepository;
import web.webbanbalo.repository.UserRepository;
import web.webbanbalo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @PostMapping
    public ResponseEntity<String> addFavorite(@RequestBody Favorite favoriteRequest) {
        // Kiểm tra null cho User
        if (favoriteRequest.getUser() == null) {
            return ResponseEntity.badRequest().body("User information is missing or invalid");
        }

        // Kiểm tra null cho Product
        if (favoriteRequest.getProduct() == null) {
            return ResponseEntity.badRequest().body("Product information is missing or invalid");
        }

        // Lấy User từ repository
        User user = userRepository.findById(favoriteRequest.getUser().getId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Lấy Product từ repository
        Product product = productRepository.findById(favoriteRequest.getProduct().getId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        // Kiểm tra xem sản phẩm đã có trong yêu thích của User chưa
        Optional<Favorite> existingFavorite = favoriteRepository.findByUserAndProduct(user, product);
        if (existingFavorite.isPresent()) {
            return ResponseEntity.badRequest().body("Product already in favorites");
        }

        // Tạo đối tượng Favorite mới và lưu vào repository
        Favorite newFavorite = new Favorite();
        newFavorite.setUser(user);
        newFavorite.setProduct(product);
        favoriteRepository.save(newFavorite);

        return ResponseEntity.ok("Product added to favorites");
    }

    // Function để lấy danh sách yêu thích của người dùng từ API
    @GetMapping("/{userId}")
    public ResponseEntity<List<Favorite>> getFavoritesByUserId(@PathVariable int userId) {
        // Tìm kiếm User trong repository
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Lấy danh sách yêu thích của User từ repository
        List<Favorite> favorites = favoriteRepository.findByUser(userOptional.get());
        return ResponseEntity.ok(favorites);
    }

    // Function để xóa yêu thích sản phẩm của người dùng
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> deleteFavorite(@PathVariable int userId, @PathVariable int productId) {
        // Tìm kiếm User trong repository
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Tìm kiếm Product trong repository
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        // Tìm và xóa yêu thích của User cho Product trong repository
        Optional<Favorite> favoriteOptional = favoriteRepository.findByUserAndProduct(userOptional.get(), productOptional.get());
        if (!favoriteOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Favorite not found");
        }

        favoriteRepository.delete(favoriteOptional.get());
        return ResponseEntity.ok("Favorite deleted successfully");
    }
}