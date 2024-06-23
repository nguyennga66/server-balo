package web.webbanbalo.entity;

import jakarta.persistence.*;

@Entity
public class View {
    @Id
    @GeneratedValue
    private int id;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int viewCount;

    public View() {
    }

    public View(int id, Product product, int viewCount) {
        this.id = id;
        this.product = product;
        this.viewCount = viewCount;
    }

    public View(Product product, int viewCount) {
        this.product = product;
        this.viewCount = viewCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "View{" +
                "id=" + id +
                ", product=" + product +
                ", viewCount=" + viewCount +
                '}';
    }
}
