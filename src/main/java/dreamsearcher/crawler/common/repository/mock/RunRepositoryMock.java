package dreamsearcher.crawler.common.repository.mock;

import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.repository.RunRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("test")
public class RunRepositoryMock implements RunRepository {


    @Override
    public Iterable<Run> findAllByIsProcessedTrue() {
        List<Run> runs = new ArrayList<>();
        runs.add(new Run("UUID-1111",
                "Fri Mar 01 00:00:01 GMT+04:00 2022",
                "Citilink",
                true,
                null));
        runs.add(new Run("UUID-2222",
                "Fri Mar 01 00:00:02 GMT+04:00 2022",
                "Citilink",
                true,
                null));
        runs.add(new Run("UUID-3333",
                "Fri Mar 01 00:00:03 GMT+04:00 2022",
                "Citilink",
                true,
                null));
        return runs;
    }

    @Override
    public Iterable<Run> findAllByIsProcessedFalse() {
        List<Run> runs = new ArrayList<>();
        runs.add(new Run("UUID-1111",
                "Fri Mar 02 00:00:01 GMT+04:00 2022",
                "DNS",
                false,
                null));
        runs.add(new Run("UUID-2222",
                "Fri Mar 02 00:00:02 GMT+04:00 2022",
                "DNS",
                false,
                null));
        return runs;
    }

    //Next not implemented as unnecessary

    @Override
    public <S extends Run> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Run> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Run> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Run> findAll() {
        return null;
    }

    @Override
    public Iterable<Run> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {}

    @Override
    public void delete(Run entity) {}

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {}

    @Override
    public void deleteAll(Iterable<? extends Run> entities) {}

    @Override
    public void deleteAll() {}
}
