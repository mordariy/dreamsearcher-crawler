package dreamsearcher.crawler.common.repository.mock;

import dreamsearcher.crawler.common.entity.Item;
import dreamsearcher.crawler.common.repository.ItemRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("test")
public class ItemRepositoryMock implements ItemRepository {

    @Override
    public Iterable<Item> findAllByRunRunId(String runId) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("UUID-1111",
                null,
                "PocoPhone bablablavovich 12",
                "URL-1",
                349999.99
                ));
        items.add(new Item("UUID-2222",
                null,
                "PocoPhone bablablavovich 11",
                "URL-2",
                229999.99
        ));
        return items;
    }

    //Next not implemented as unnecessary

    @Override
    public <S extends Item> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Item> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Item> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Item> findAll() {
        return null;
    }

    @Override
    public Iterable<Item> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Item entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Item> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
