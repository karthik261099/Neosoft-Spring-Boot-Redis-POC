package com.example.redis.config;

import com.example.redis.domain.port.UserPort;
import com.example.redis.infrastructure.entity.User;
import org.redisson.Redisson;
import org.redisson.api.MapOptions;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;

@Configuration
public class ReddisonConfig {

    @Autowired
    private UserPort userPort;

    Logger logger = LoggerFactory.getLogger(ReddisonConfig.class);

    MapWriter<Long, User> mapWriter = new MapWriter<Long, User>() {


        @Override
        public void write(Map<Long, User> map) {

            logger.info("******************* write(Map<Long, User> map)");

            for (Map.Entry<Long, User> entry : map.entrySet()) {
                userPort.createUser(entry.getValue());
            }

        }

        @Override
        public void delete(Collection<Long> keys) {

            logger.info("******************* delete(Collection<Long> keys)");

            for (Long key : keys) {
                userPort.deleteById(key);

            }

        }


    };

//    Config config = new Config();
    RedissonClient redisson = Redisson.create();

    MapOptions<Long, User> options = MapOptions.<Long, User>defaults()
            .writer(mapWriter)
            .writeMode(MapOptions.WriteMode.WRITE_THROUGH);


    RMap<Long, User> map = redisson.getMap("users", options);
//    // or
//    RMapCache<Long, User> map = redisson.getMapCache("test", options);
//    // or with boost up to 45x times
//    RLocalCachedMap<Long, User> map = redisson.getLocalCachedMap("test", options);


}
