package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.entity.Run;
import dreamsearcher.crawler.common.repository.RunRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RunService {

    private final RunRepository runRepository;

    public RunService(RunRepository runRepository) {
        this.runRepository = runRepository;
    }

    public List<Run> getIfProcessed(boolean isProcessed) {
        if (isProcessed) {
            return (List<Run>) runRepository.findAllByIsProcessedTrue();
        } else {
            return (List<Run>) runRepository.findAllByIsProcessedFalse();
        }
    }

    public Run create(String runShopName) {
        return runRepository.save(Run.builder()
                .dateTime(new Date().toString())
                .shopName(runShopName)
                .isProcessed(false)
                .build());
    }

    public Run update(Run run) {
        return runRepository.save(run);
    }
}
