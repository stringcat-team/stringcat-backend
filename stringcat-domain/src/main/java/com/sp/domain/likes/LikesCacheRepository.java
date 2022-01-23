package com.sp.domain.likes;

import com.sp.common.utils.RedisKeyUtils;
import com.sp.common.utils.RedisKeyUtils.KeyPrefix;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikesCacheRepository {
    private final int DELTA_VALUE = 1;

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public Map<Long, Boolean> findPushed(KeyPrefix keyPrefix, List<Long> ids, Long userId) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        String strUserId = String.valueOf(userId);

        List<String> keys = RedisKeyUtils.generateKeys(keyPrefix, ids);
        List<Object> pushed = redisTemplate.executePipelined(
            (RedisCallback<Object>) connection -> {
                keys.forEach(key -> {
                    connection.sIsMember(keySerializer.serialize(key), valueSerializer.serialize(strUserId));
                });
                return null;
            });

        Map<Long, Boolean> pushedMap = new HashMap<>();

        for(int i = 0; i < pushed.size(); i++) {
            pushedMap.put(ids.get(i), (Boolean) pushed.get(i));
        }

        return pushedMap;
    }

    public Map<Long, Integer> findCount(KeyPrefix keyPrefix, List<Long> ids) {
        List<String> keys = RedisKeyUtils.generateKeys(keyPrefix, ids);
        List<String> counts = stringRedisTemplate.opsForValue().multiGet(keys);

        Map<Long, Integer> countMap = new HashMap<>();

        for(int i = 0; i < counts.size(); i++) {
            String count = counts.get(i);
            countMap.put(ids.get(i), count == null ? 0 : Integer.parseInt(count));
        }

        return countMap;
    }

    public boolean exists(KeyPrefix keyPrefix, Long userId, Long entityId) {
        Boolean exists = redisTemplate.opsForSet().isMember(RedisKeyUtils.generateKey(keyPrefix, entityId), userId);
        return exists != null && exists != Boolean.FALSE;
    }

    public void add(KeyPrefix keyPrefix, Long userId, Long entityId) {
        redisTemplate.opsForSet().add(RedisKeyUtils.generateKey(keyPrefix, entityId), userId);
    }

    public void remove(KeyPrefix keyPrefix, Long userId, Long entityId) {
        redisTemplate.opsForSet().remove(RedisKeyUtils.generateKey(keyPrefix, entityId), userId);
    }

    public void increase(KeyPrefix keyPrefix, Long entityId) {
        redisTemplate.opsForValue().increment(RedisKeyUtils.generateKey(keyPrefix, entityId), DELTA_VALUE);
    }

    public void decrease(KeyPrefix keyPrefix, Long entityId) {
        redisTemplate.opsForValue().decrement(RedisKeyUtils.generateKey(keyPrefix, entityId), DELTA_VALUE);
    }
}
