package com.weiquding.scheduling.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 查询示例
 *
 * @author beliveyourself
 * @version V1.0
 * @date 2020/2/15
 */
@Service
public class QueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryService.class);

    /**
     * JVM数据缓存：单写多读。
     */
    private volatile Map<String, Object> cache;

    /**
     * 计数器,用于模拟数据库无数据的情况。
     */
    private AtomicInteger count = new AtomicInteger();

    /**
     * 联机接口从JVM缓存中取数，相当于实际取数逻辑上包装一层，用于Controller层调用。
     *
     * @return 业务数据
     */
    public Map<String, Object> queryDataFromCache() {
        if (cache != null && !cache.isEmpty()) {
            return cache;
        }
        LOGGER.warn("联机查询未取得数据，开始直接查询数据库");
        scheduleFixedDelayTask();
        return cache;
    }


    /**
     * 实际的数据查询方法。
     *
     * @return 业务数据
     */
    private Map<String, Object> queryDataFormDB() {
        if (count.getAndIncrement() % 2 == 1) {
            // 模拟空集合
            LOGGER.warn("数据库没有取得数据，当前计数：{}", count.get());
            return new HashMap<>();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("key", "测试数据");
        return data;
    }

    /**
     * 定时任务，5分钟执行一次
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public synchronized void scheduleFixedDelayTask() {
        Map<String, Object> data = queryDataFormDB();
        LOGGER.warn("定时任务调度，取得数据: {}", data);
        if (data != null && !data.isEmpty()) {
            // 只有有数据时才覆盖缓存。
            cache = data;
        }
    }

}
