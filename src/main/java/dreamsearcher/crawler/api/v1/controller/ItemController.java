package dreamsearcher.crawler.api.v1.controller;

import dreamsearcher.crawler.common.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity getItems(@RequestParam(value = "runId", required = true) String runId) {
        return ResponseEntity.ok(itemService.getItemsByRunId());
    }

}