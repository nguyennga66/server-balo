package web.webbanbalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.webbanbalo.entity.Bill;
import web.webbanbalo.entity.Review;
import web.webbanbalo.entity.User;

import web.webbanbalo.repository.BillRepository;
import web.webbanbalo.repository.ReviewRepository;
import web.webbanbalo.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository; // Bổ sung BillRepository

    // Tạo mới một đánh giá
    @CrossOrigin(origins = "*")
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        // Kiểm tra xem user_id và product_id đã được thiết lập
        if (review.getUser() == null || review.getProduct() == null) {
            return ResponseEntity.badRequest().body("User hoặc Product không được trống");
        }

        // Kiểm tra xem người dùng đã mua sản phẩm hay chưa
        Optional<Bill> bill = billRepository.findFirstByCartUserAndBillDetailsProduct(review.getUser().getId(), review.getProduct().getId());
        if (bill.isEmpty()) {
            return ResponseEntity.badRequest().body("Bạn phải mua sản phẩm này trước khi đánh giá.");
        }
            // Lấy thông tin user từ cơ sở dữ liệu để sử dụng
            Optional<User> userOptional = userRepository.findById(review.getUser().getId());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("User không tồn tại trong hệ thống.");
            }
            User user = userOptional.get();

            // Đặt lại user để đảm bảo có thông tin đầy đủ
            review.setUser(user);

            // Đặt createDate là ngày và giờ hiện tại
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            review.setCreateDate(formatter.format(new Date()));

            // Lưu review vào cơ sở dữ liệu
            Review savedReview = reviewRepository.save(review);

            // Trả về review đã lưu thành công
            return ResponseEntity.ok(savedReview);
        }

        @CrossOrigin(origins = "*")
        @GetMapping("/reviews/{productId}")
        public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable int productId) {
            List<Review> reviews = reviewRepository.findByProductIdWithUser(productId);
            return ResponseEntity.ok(reviews);
        }

        // Xóa đánh giá
        @CrossOrigin(origins = "*")
        @DeleteMapping("/reviews/{reviewId}")
        public ResponseEntity<Void> deleteReview(@PathVariable int reviewId) {
            Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
            if (reviewOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            reviewRepository.delete(reviewOptional.get());
            return ResponseEntity.noContent().build();
        }
    }