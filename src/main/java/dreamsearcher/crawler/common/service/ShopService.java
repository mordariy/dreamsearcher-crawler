package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.entity.Shop;
import dreamsearcher.crawler.common.repo.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    private ShopRepository ShopRepository;

    public ShopService(ShopRepository ShopRepository) {
        this.ShopRepository = ShopRepository;
    }

    public List<Shop> getShops() {
        return (List<Shop>) ShopRepository.findAll();
    }
    
}
