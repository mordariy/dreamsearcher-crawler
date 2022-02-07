package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.repo.RunRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunService {

    private RunRepository RunRepository;

    public RunService(RunRepository RunRepository) {
        this.RunRepository = RunRepository;
    }

    public List<Run> getRuns() {
        return (List<Run>) RunRepository.findAll();
    }

    public void saveRun(Run run) {
        RunRepository.save(run);
    }
    
}
