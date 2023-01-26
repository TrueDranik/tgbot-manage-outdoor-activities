package com.bot.sup;

import com.bot.sup.cache.UserStateCache;
import com.bot.sup.common.enums.states.InstructorStateEnum;
import com.bot.sup.model.UserState;
import com.bot.sup.model.entity.Instructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class CacheTest extends AbstractTest {

    @Autowired
    UserStateCache stateCache;

    @Test
    void testCache() {
        Object entity = new Instructor();
        UserState state = new UserState();
        state.setAdminTelegramId(432423432L);
        state.setEntity(entity);
        state.setState(InstructorStateEnum.FILLING_INSTRUCTOR);

        stateCache.createOrUpdateState(state);
        stateCache.createOrUpdateState(state);

        UserState userState = stateCache.getByTelegramId(432423432L);
        log.info("Got user - {}", userState);
        stateCache.deleteFromCache(432423432L);
//        UserState userState2 = stateCache.getByTelegramId(432423432L);
//        log.info("Got user - {}", userState2);
    }
}
