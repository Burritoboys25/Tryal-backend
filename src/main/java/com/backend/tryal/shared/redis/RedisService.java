package com.backend.tryal.shared.redis;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

@Service
public class RedisService {

    private final JedisPool jedisPool;

    public RedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setValue(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }

    public void setValueWithExpiry(String key, String value, long seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            jedis.expire(key, seconds);
        }
    }

    public String getValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        }
    }

    public boolean exists(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(key);
        }
    }

    public void delete(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        }
    }

    public boolean setIfAbsent(String key, String value, long expirySeconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            String result = jedis.set(key, value, SetParams.setParams().nx().ex(expirySeconds));
            return "OK".equals(result);
        }
    }
}
