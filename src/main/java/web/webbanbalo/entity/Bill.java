package web.webbanbalo.entity;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private String fullName;
    private String address;
    private String email;
    private String phone;
    private String orderNotes;
    private String createDate;
    private int status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "bill_billdetail",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "bill_detail_id")
    )
    private List<BillDetail> billDetails;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int total;
    private int shippingFee;
    private int grandTotal;

    // Constructors, getters, setters
    public Bill() {
    }

    public Bill(int userId, String fullName, String address, String email, String phone, String orderNotes, int status, List<BillDetail> billDetails, Cart cart, int total, int shippingFee, int grandTotal) {
        this.userId = userId;
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.orderNotes = orderNotes;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        this.createDate = formatter.format(new Date());
        this.status = status;
        this.billDetails = billDetails;
        this.cart = cart;
        this.total = total;
        this.shippingFee = shippingFee;
        this.grandTotal = grandTotal;
    }

    @Override
    public String toString() {
        return "BillDetail{" +
                "id=" + id +
                ", companyName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", orderNotes='" + orderNotes + '\'' +
                ", status='"+ status + '\'' +
                ", total=" + total +
                ", shippingFee=" + shippingFee +
                ", grandTotal=" + grandTotal +
                '}';
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
