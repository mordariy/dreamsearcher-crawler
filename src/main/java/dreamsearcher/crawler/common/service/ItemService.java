package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.repo.ItemRepository;
import dreamsearcher.crawler.common.entity.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItems() {
        return (List<Item>) itemRepository.findAll();
    }

    public void saveItems(List<Item> items) {
        itemRepository.saveAll(items);
    }

}
