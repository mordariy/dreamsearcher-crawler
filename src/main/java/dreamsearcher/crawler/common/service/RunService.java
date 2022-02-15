package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.repository.RunRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunService {

    private RunRepository runRepository;

    public RunService(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    public List<Run> getRuns() {
        return (List<Run>) runRepository.findAll();
    }

/*
    public List<Run> getRunsIfProcessed(boolean processed) {
        return (List<Run>) runRepository.findAllByProcessed(Boolean.toString(processed));
    }
*/

    public void saveRun(Run run) {
        runRepository.save(run);
    }
    
}
