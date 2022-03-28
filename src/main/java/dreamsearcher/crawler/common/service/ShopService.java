package dreamsearcher.crawler.common.service;

import dreamsearcher.crawler.common.parser.Parser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {

    private final List<Parser> parsers;

    public ShopService(List<Parser> parsers) {
        this.parsers = parsers;
    }

    public List<String> getAll() {
        List<String> shops = new ArrayList<>();
        for (Parser parser:parsers) {
            shops.add(parser.getShop());
        }
        return shops;
    }
}
