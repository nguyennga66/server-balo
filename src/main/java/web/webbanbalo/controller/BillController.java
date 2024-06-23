package web.webbanbalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.webbanbalo.entity.*;
import web.webbanbalo.repository.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class BillController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @CrossOrigin(origins = "*")
    @PostMapping("/createBill")
    public ResponseEntity<String> createBillDetail(@RequestBody Bill bill) {
        // Kiểm tra xem đối tượng CartItem có chứa một đối tượng Cart hợp lệ không
        Cart cart = bill.getCart();

        int userId = cart.getUser().getId();
        // Kiểm tra xem người dùng có tồn tại không
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Kiểm tra xem giỏ hàng của người dùng có tồn tại không
        Cart existingCart = cartRepository.findByUser(user);

        // Lưu vào cơ sở dữ liệu
        bill.setCart(existingCart);

        bill.setStatus(0);

        // Khởi tạo ngày tạo hóa đơn theo định dạng dd/mm/yy - time
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        bill.setCreateDate(formatter.format(new Date()));

        billRepository.save(bill);

        // Lấy danh sách các mục trong giỏ hàng của người dùng
        List<CartItem> cartItems = cartItemRepository.findByCart(existingCart);
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("No items in cart");
        }

        // Xóa tất cả các mục trong giỏ hàng
        cartItemRepository.deleteAll(cartItems);

        return ResponseEntity.ok().body("Hóa đơn được tạo thành công.");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/bills")
    public ResponseEntity<?> getBillDetails(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Bill> billDetailsPage = billRepository.findAll(pageable);
        return ResponseEntity.ok(billDetailsPage);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/bills/user/{userId}")
    public ResponseEntity<?> getBillDetailsByUserId(@PathVariable int userId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Bill> billDetailsPage = billRepository.findByUserId(userId, pageable);
        return ResponseEntity.ok(billDetailsPage);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/bills/{billId}")
    public ResponseEntity<?> getBillDetail(@PathVariable int billId) {
        Optional<Bill> billDetail = billRepository.findById(billId);
        if (billDetail.isPresent()) {
            return ResponseEntity.ok(billDetail.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hóa đơn không tồn tại.");
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/approve/{billId}")
    public ResponseEntity<?> approveOrder(@PathVariable int billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setStatus(1); // đang vận chuyển
            billRepository.save(bill);
            return ResponseEntity.ok(bill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/cancel/{billId}")
    public ResponseEntity<?> cancel(@PathVariable int billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setStatus(2); // đang vận chuyển
            billRepository.save(bill);
            return ResponseEntity.ok(bill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}