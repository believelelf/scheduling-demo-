package com.weiquding.scheduling.controller;

import com.weiquding.scheduling.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 查询
 *
 * @author beliveyourself
 * @version V1.0
 * @date 2020/2/15
 */
@RestController
public class QueryController {

    @Autowired
    private QueryService queryService;

    @RequestMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> query() {
        return queryService.queryDataFromCache();
    }

}
