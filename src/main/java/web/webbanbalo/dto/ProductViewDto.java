package web.webbanbalo.dto;

public class ProductViewDto {
    private Integer id;
    private Integer viewCount;

    public ProductViewDto() {
    }

    public ProductViewDto(Integer id, Integer viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
