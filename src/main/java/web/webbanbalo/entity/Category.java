package web.webbanbalo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private int id;

    private String nameC;

    @OneToMany(mappedBy="category")
    @JsonIgnore
    private List<Product> product;

    public Category(int id, String nameC) {
        this.id = id;
        this.nameC = nameC;
    }

    public Category() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameC() {
        return nameC;
    }

    public void setNameC(String nameC) {
        this.nameC = nameC;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", nameC='" + nameC + '\'' +
                ", product=" + product +
                '}';
    }
}
