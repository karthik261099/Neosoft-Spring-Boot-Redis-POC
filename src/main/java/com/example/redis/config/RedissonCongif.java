package com.example.redis.config;

import com.example.redis.infrastructure.adaptor.UserJPAAdapter;
import com.example.redis.infrastructure.entity.User;
import org.redisson.Redisson;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@Transactional
public class RedissonCongif implements InitializingBean {

    private final String CACHE_NAME="users";

    private RedissonClient redissonClient;

    Logger logger = LoggerFactory.getLogger(RedissonCongif.class);

    @Autowired
    private UserJPAAdapter userJPAAdapter;

    @Bean
    public RMapCache<Long, User> userRMapCache() {
        final RMapCache<Long, User> userRMapCache = redissonClient.getMapCache(CACHE_NAME,MapOptions.<Long, User>defaults()
                .writer(getMapWriter()).loader(getMapLoader())
                .writeMode(MapOptions.WriteMode.WRITE_THROUGH));
        return userRMapCache;
    }

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        config.useMasterSlaveServers().setMasterAddress("redis://localhost:6379");
        return Redisson.create(config);
    }

    private MapWriter<Long, User> getMapWriter() {
        return new MapWriter<Long, User>() {

            @Override
            public void write(final Map<Long, User> map) {
                logger.info("*********************** write");
                map.forEach( (k, v) -> {
                    userJPAAdapter.createUser(v);
                });
            }

            @Override
            public void delete(Collection<Long> keys) {
                logger.info("*********************** delete");
                keys.stream().forEach(e -> {
                    userJPAAdapter.deleteById(e);
                });
            }
        };
    }

    private MapLoader<Long, User> getMapLoader() {
        return new MapLoader<Long, User>() {

            @Override
            public Iterable<Long> loadAllKeys() {

                logger.info("*********************** Iterable<Long> loadAllKeys()");
                List<Long> list = new ArrayList<Long>(){};

                list.add(1L);
                list.add(2L);
                list.add(3L);
                list.add(4L);
                list.add(33L);
                list.add(34L);

                return list;
            }

            @Override
            public User load(Long key) {
                logger.info("*********************** User load(Long key)");
//                return userRepository.getById(key);
                return userJPAAdapter.findById(key).get();
            }

        };
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        final Config config = new Config();
        config.useMasterSlaveServers().setMasterAddress("redis://localhost:6379");
        this.redissonClient = Redisson.create(config);
    }


}
