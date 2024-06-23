package web.webbanbalo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.webbanbalo.entity.Bill;
import web.webbanbalo.entity.View;
import web.webbanbalo.repository.BillRepository;
import web.webbanbalo.repository.FavoriteRepository;
import web.webbanbalo.repository.ProductRepository;
import web.webbanbalo.repository.ViewRepository;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class StatsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping("/revenue")
    public double getTotalRevenue() {
        List<Bill> bills = billRepository.findAll();
        double totalRevenue = 0.0;
        for (Bill bill : bills) {
            totalRevenue += bill.getGrandTotal();
        }
        return totalRevenue;
    }

    @GetMapping("/totalViews")
    public double getTotalView() {
        List<View> views = viewRepository.findAll();
        double totalView = 0;
        for (View view : views) {
            totalView += view.getViewCount();
        }
        return totalView;
    }

    @GetMapping("/totalFavorites")
    public double getTotalFavorites() {
        int totalFavorite = favoriteRepository.findAll().size();
        return totalFavorite;
    }
}
