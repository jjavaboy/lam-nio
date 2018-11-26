package lam.study.gateway.httpapplication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: linanmiao
 */
@RestController
@RequestMapping("index")
public class IndexControlller {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexControlller.class);

    @RequestMapping("/")
    public Map<String, Object> index() {
        LOGGER.info("[index]");

        Map<String, Object> map = new HashMap<String, Object>();
        return map;
    }

    @RequestMapping("{id}")
    public Map<String, Object> getById(@PathVariable("id") Long id) {
        LOGGER.info("[getById]");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("username", "LAM");
        return map;
    }

}
