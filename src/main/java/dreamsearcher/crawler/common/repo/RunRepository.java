package dreamsearcher.crawler.common.repo;

import dreamsearcher.crawler.common.entity.Run;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends CrudRepository<Run, String> {}
