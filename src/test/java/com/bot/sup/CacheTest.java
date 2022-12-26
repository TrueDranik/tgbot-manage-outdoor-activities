package com.bot.sup;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.model.UserState;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class CacheTest extends AbstractTest {

    @Autowired
    UserStateCache stateCache;

    @Test
    void testCache() {
        UserState state = new UserState();
        state.setAdminTelegramId(432423432L);
        state.setInstructorTelegramId(6546456L);
        state.setState("START");
        stateCache.createOrUpdateState(state);
        stateCache.createOrUpdateState(state);
        UserState userState = stateCache.getByTelegramId(432423432L);
        log.info("Got user - {}", userState);
        stateCache.deleteFromCache(432423432L);
//        UserState userState2 = stateCache.getByTelegramId(432423432L);
//        log.info("Got user - {}", userState2);
    }
}
