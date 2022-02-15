package dreamsearcher.crawler.common.repository;

import dreamsearcher.crawler.common.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, String> {

    Iterable<Item> findAllByRunId(String runId);

}