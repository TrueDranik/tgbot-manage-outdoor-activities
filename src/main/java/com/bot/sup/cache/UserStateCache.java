package com.bot.sup.cache;

import com.bot.sup.model.UserState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStateCache {
    private final CacheManager cacheManager;

    @Cacheable(value = "states")
    public UserState getByTelegramId(Long telegramId) {
        Cache states = cacheManager.getCache("states");
        return (UserState) Optional.ofNullable(states).map(it -> it.get(telegramId)).map(Cache.ValueWrapper::get)
                .orElseThrow(() -> new IllegalArgumentException("Cache with tgId - %s not found".formatted(telegramId)));
    }

    @CachePut(value = "states", key = "#state.adminTelegramId")
    public UserState createOrUpdateState(UserState state) {
        log.info("Saving user state with values - [{}:{}]", state.getState(), state.getAdminTelegramId());
        return state;
    }

    @CacheEvict(value = "states", key = "#telegramId")
    public void deleteFromCache(Long telegramId) {
        log.info("Deleting user state with tgId - [{}]", telegramId);
    }
}
