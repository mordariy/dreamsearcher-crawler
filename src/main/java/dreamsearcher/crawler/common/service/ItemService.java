package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.repository.ItemRepository;
import dreamsearcher.crawler.common.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItemsByRunId(String runId) {
        return (List<Item>) itemRepository.findAllByRunId(runId);
    }

    public void saveItems(List<Item> items) {
        itemRepository.saveAll(items);
    }

}
