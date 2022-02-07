package dreamsearcher.crawler.common.repo;

import dreamsearcher.crawler.common.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends CrudRepository<Shop, String> {}
