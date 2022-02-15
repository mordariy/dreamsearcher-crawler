package dreamsearcher.crawler.api.v1.controller;

import dreamsearcher.crawler.common.service.RunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/runs")
public class RunController {

    private RunService runService;

    public RunController(RunService runService) {
        this.runService = runService;
    }
/*
    @GetMapping
    public ResponseEntity getRuns(@RequestParam(value="processed", required = true) boolean processed) {
        return ResponseEntity.ok(runService.getRunsIfProcessed(processed));
    }
*/

    @GetMapping
    public ResponseEntity getRuns() {
        return ResponseEntity.ok(runService.getRuns());
    }

}