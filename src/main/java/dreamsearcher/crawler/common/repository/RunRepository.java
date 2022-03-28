package dreamsearcher.crawler.common.repository;

import dreamsearcher.crawler.common.entity.Run;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
public interface RunRepository extends CrudRepository<Run, String> {

    Iterable<Run> findAllByIsProcessedTrue();
    Iterable<Run> findAllByIsProcessedFalse();

}
