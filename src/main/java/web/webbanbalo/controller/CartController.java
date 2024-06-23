    package web.webbanbalo.controller;

    import org.antlr.v4.runtime.misc.NotNull;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import web.webbanbalo.entity.Cart;
    import web.webbanbalo.entity.CartItem;
    import web.webbanbalo.entity.Product;
    import web.webbanbalo.entity.User;
    import web.webbanbalo.repository.CartItemRepository;
    import web.webbanbalo.repository.CartRepository;
    import web.webbanbalo.repository.ProductRepository;
    import web.webbanbalo.repository.UserRepository;

    import java.util.List;
    import java.util.Map;
    import java.util.Optional;

    @CrossOrigin(origins = "*")
    @RestController
    public class CartController {
        @Autowired
        private UserRepository userRepository;

        @Autowired
        private CartItemRepository cartItemRepository;

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private ProductRepository productRepository;

        @PostMapping("/carts")
        public ResponseEntity<String> addToCart(@RequestBody CartItem cartItemRequest) {
            // Kiểm tra xem đối tượng CartItem có chứa một đối tượng Cart hợp lệ không
            Cart cart = cartItemRequest.getCart();
            if (cart == null) {
                return ResponseEntity.badRequest().body("Cart not found in request");
            }

            int userId = cart.getUser().getId();

            // Kiểm tra xem người dùng có tồn tại không
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Kiểm tra xem sản phẩm có tồn tại không
            Product product = productRepository.findById(cartItemRequest.getProduct().getId()).orElse(null);
            if (product == null) {
                return ResponseEntity.badRequest().body("Product not found");
            }

            // Kiểm tra xem giỏ hàng của người dùng có tồn tại không
            Cart existingCart = cartRepository.findByUser(user);
            if (existingCart == null) {
                // Nếu không có giỏ hàng, tạo một giỏ hàng mới cho người dùng
                existingCart = new Cart();
                existingCart.setUser(user);
                cartRepository.save(existingCart);
            }

            // Kiểm tra xem mục giỏ hàng đã tồn tại chưa
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProduct(existingCart, product);
            CartItem existingCartItem;
            if (optionalCartItem.isEmpty()) {
                // Nếu không có mục giỏ hàng, tạo mới
                existingCartItem = new CartItem();
                existingCartItem.setCart(existingCart);
                existingCartItem.setProduct(product);
                existingCartItem.setQuantity(cartItemRequest.getQuantity());
            } else {
                // Nếu có mục giỏ hàng, cập nhật số lượng
                existingCartItem = optionalCartItem.get();
                existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            }

            // Lưu mục giỏ hàng vào cơ sở dữ liệu
            cartItemRepository.save(existingCartItem);

            // Trả về phản hồi thành công
            return ResponseEntity.ok("Product added to cart");
        }

        @GetMapping("/carts/{userId}")
        public ResponseEntity<?> getCartItemsByUserId(@PathVariable int userId) {
            // Kiểm tra xem người dùng có tồn tại không
            User user = userRepository.findById(userId).get();
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Lấy giỏ hàng của người dùng
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                return ResponseEntity.ok("Cart is empty");
            }

            // Lấy danh sách các mục giỏ hàng (cart items)
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            if (cartItems.isEmpty()) {
                return ResponseEntity.ok("Cart is empty");
            } else {
                return ResponseEntity.ok(cartItems);
            }
        }

        @PutMapping("/carts/{userId}/{productId}")
        public ResponseEntity<String> updateCartItemQuantity(@PathVariable int userId, @PathVariable int productId, @RequestBody Map<String, Integer> requestBody) {
            // Kiểm tra xem người dùng có tồn tại không
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Kiểm tra xem sản phẩm có tồn tại không
            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                return ResponseEntity.badRequest().body("Product not found");
            }

            // Kiểm tra xem giỏ hàng của người dùng có tồn tại không
            Cart cart = cartRepository.findByUser(user);
            if (cart == null) {
                return ResponseEntity.badRequest().body("Cart not found");
            }

            // Tìm mục giỏ hàng để cập nhật số lượng
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (!optionalCartItem.isPresent()) {
                return ResponseEntity.badRequest().body("Cart item not found");
            }

            CartItem cartItem = optionalCartItem.get();
            // Cập nhật số lượng của mục giỏ hàng
            int newQuantity = requestBody.get("quantity");
            if (newQuantity <= 0) {
                return ResponseEntity.badRequest().body("Invalid quantity");
            }
            cartItem.setQuantity(newQuantity);

            // Lưu thay đổi vào cơ sở dữ liệu
            cartItemRepository.save(cartItem);

            return ResponseEntity.ok("Cart item quantity updated");
        }


        @DeleteMapping("/carts")
            public ResponseEntity<String> removeFromCart(@RequestBody @NotNull CartItem cartItemRequest) {

                // Lấy người dùng từ đối tượng CartItem
                User user = cartItemRequest.getCart().getUser();

                // Kiểm tra xem người dùng có tồn tại không
                user = userRepository.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));


                // Lấy ID của giỏ hàng và sản phẩm từ yêu cầu
                int cartId = cartItemRequest.getCart().getId();
                int productId = cartItemRequest.getProduct().getId();

                // Kiểm tra xem giỏ hàng và sản phẩm có tồn tại không
                Cart cart = cartRepository.findByUser(user);
                if (cart == null) {
                    return ResponseEntity.badRequest().body("Cart not found");
                }

                Product product = productRepository.findById(productId).orElse(null);
                if (product == null) {
                    return ResponseEntity.badRequest().body("Product not found");
                }

                // Tìm mục giỏ hàng để xóa
                Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProduct(cart, product);
                if (!optionalCartItem.isPresent()) {
                    return ResponseEntity.badRequest().body("Cart item not found");
                }

                // Xóa mục giỏ hàng khỏi giỏ hàng
                CartItem cartItem = optionalCartItem.get();
                cartItemRepository.delete(cartItem);

                // Trả về phản hồi thành công
                return ResponseEntity.ok("Product removed from cart");
            }

    }
