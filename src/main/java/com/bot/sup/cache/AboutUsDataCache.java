package com.bot.sup.cache;

import com.bot.sup.common.enums.AboutUsStateEnum;
import com.bot.sup.model.entity.AboutUs;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

@Component
public class AboutUsDataCache {
    private final Map<Long, AboutUsStateEnum> aboutUsState = new WeakHashMap<>();
    private final Map<Long, AboutUs> aboutUsData = new WeakHashMap<>();
    private final Map<Long, Long> aboutUsForUpdate = new WeakHashMap<>();

    public void setAboutUsCurrentState(Long chatId, AboutUsStateEnum registrationState) {
        aboutUsState.put(chatId, registrationState);
    }

    public AboutUsStateEnum getAboutUsCurrentState(Long chatId) {
        return aboutUsState.getOrDefault(chatId, AboutUsStateEnum.FILLING_ABOUT_US);
    }

    public void removeAboutUsCurrentState(Long chatId) {
        aboutUsState.remove(chatId);
    }

    public AboutUs getAboutUsProfileData(Long chatId) {
        return aboutUsData.getOrDefault(chatId, new AboutUs());
    }

    public void saveAboutUsProfileData(Long chatId, AboutUs aboutUs) {
        aboutUsData.put(chatId, aboutUs);
    }

    public void saveAboutUsForUpdate(Long chatId, Long activityFormatId) {
        aboutUsForUpdate.put(chatId, activityFormatId);
    }

    public void removeAboutUsForUpdate(Long chatId) {
        aboutUsForUpdate.remove(chatId);
    }

    public Long getAboutUsForUpdate(Long chatId) {
        return aboutUsForUpdate.get(chatId);
    }
}
