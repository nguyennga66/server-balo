package web.webbanbalo.controller;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.webbanbalo.entity.User;
import web.webbanbalo.repository.UserRepository;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PutMapping("/admin/{userId}")
    public ResponseEntity<User> updateRoleUser(@PathVariable int userId, @RequestBody User userUpdate){
        try{
            Optional<User> userOptional = userRepository.findById(userId);
            if (!userOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            User user = userOptional.get();
            user.setRole(userUpdate.getRole());
            userRepository.save(user);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        User user = userRepository.findById(userId).get();
        return user;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable int userId,
            @RequestBody User userToUpdate) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User existingUser = optionalUser.get();
                existingUser.setFullName(userToUpdate.getFullName());
                existingUser.setPhone(userToUpdate.getPhone());
                existingUser.setEmail(userToUpdate.getEmail());
                existingUser.setAddress(userToUpdate.getAddress());

                User updatedUser = userRepository.save(existingUser);

                if (updatedUser != null) {
                    return ResponseEntity.ok(updatedUser);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/pass/{userId}")
    public ResponseEntity<String> changePass(@PathVariable int userId, @RequestBody Map<String, String> passwordMap) {
        try {
            // Tìm người dùng cần thay đổi mật khẩu trong cơ sở dữ liệu
            Optional<User> optionalUser = userRepository.findById(userId);

            // Kiểm tra xem người dùng có tồn tại không
            if (!optionalUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            User user = optionalUser.get();

            // Kiểm tra xem passwordMap có chứa trường password mới không
            if (!passwordMap.containsKey("oldPassword") || !passwordMap.containsKey("newPassword")) {
                return ResponseEntity.badRequest().build();
            }

            String oldPassword = passwordMap.get("oldPassword");
            String newPassword = passwordMap.get("newPassword");

            // Mã hóa mật khẩu mới
            String encryptedNewPassword = bCryptPasswordEncoder.encode(newPassword);

            // Cập nhật mật khẩu mới cho người dùng
            user.setPassword(encryptedNewPassword);

            // Lưu thông tin người dùng đã cập nhật vào cơ sở dữ liệu
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body("Đổi mật khẩu thành công");
        } catch (Exception e) {
            System.err.println("Lỗi khi thay đổi mật khẩu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> processForgotPassword(@RequestBody Map<String, String> objEmail) {
        String email = objEmail.get("email");
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại trong hệ thống");
        }
        String newPass = generatePass();
        user.setPassword(bCryptPasswordEncoder.encode(newPass));
        userRepository.save(user);
        sendEmail(email, newPass);
        return ResponseEntity.ok("Password reset email sent successfully");
    }


    public String generatePass() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        int length = 6; // Độ dài mật khẩu mong muốn

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

    private void sendEmail(String email, String newPass){
        final String username = "20130077@st.hcmuaf.edu.vn";
        final String password = "wlzxuxbbtkqaocrp";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Cấp lại mật khẩu");
            message.setText("Mật khẩu mới của bạn là: " + newPass);

            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
