package dev.duoctm.shopappbackend.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocalizeMessage(String messageKey){
        HttpServletRequest request = WebUtils.getCurrentRequest();

        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey, null,  locale);

    }
}
