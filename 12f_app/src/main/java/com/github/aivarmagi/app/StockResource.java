package com.github.aivarmagi.app;

import com.github.aivarmagi.app.domain.model.StockItem;
import com.github.aivarmagi.app.repository.StockItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/stock", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockResource {

    private static final Logger logger = LoggerFactory.getLogger(StockResource.class);

    private Environment environment;
    private StockItemRepository stockItemRepository;

    public StockResource(Environment environment, StockItemRepository stockItemRepository) {
        this.environment = environment;
        this.stockItemRepository = stockItemRepository;
    }

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "Ping";
    }

    @RequestMapping(value = "/get-env", method = RequestMethod.GET)
    public String getEnv(@RequestParam String env) {
        return "Env" + " " + environment.getProperty(env);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public StockItem stockItem(@PathVariable("id") Long id, HttpServletResponse response) {

        StockItem stockItem = stockItemRepository.findOne(id);

        if (stockItem == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        return stockItem;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public StockItem storeInStock(@RequestBody StockItem stockItem, HttpServletResponse response) {
        StockItem storedItem = stockItemRepository.save(stockItem);

        response.setStatus(HttpStatus.CREATED.value());

        return storedItem;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public StockItem update(@RequestBody StockItem stockItem, HttpServletResponse response) {
        if (!stockItemRepository.exists(stockItem.getId())) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return stockItem;
        }

        return stockItemRepository.save(stockItem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removeFromStock(@PathVariable("id") Long id, HttpServletResponse response) {
        StockItem stockItemToRemove = stockItemRepository.findOne(id);

        if (stockItemToRemove == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        stockItemRepository.delete(stockItemToRemove);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<StockItem> items() {
        return stockItemRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public StockItem getById(@PathVariable("id") Long id, HttpServletResponse response) {
        logger.info("Starting search of stock item(id={}) search", id);

        StockItem stockItem = stockItemRepository.findOne(id);

        if (stockItem == null) {
            logger.info("Stock item(id={}) has not been found", id);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }

        logger.info("Finishing search of stock item(id={}) search", id);
        return stockItem;
    }

}

