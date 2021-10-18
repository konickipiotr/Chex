package com.chex.lang;

import org.springframework.context.i18n.LocaleContextHolder;


public class LanguageUtils {

    private final LanguageGetter nameGetter;
    private LanguageGetter descriptionGetter;

    public LanguageUtils(LanguageGetter nameGetter, LanguageGetter descriptionGetter) {
        this.nameGetter = nameGetter;
        this.descriptionGetter = descriptionGetter;
    }

    public LanguageUtils(LanguageGetter nameGetter) {
        this.nameGetter = nameGetter;
    }

    public String getName(){
        if(nameGetter == null)
            return null;
        String language = LocaleContextHolder.getLocale().getLanguage();
        switch (language){
            case "pl": return nameGetter.getPl();
            default: return nameGetter.getEng();
        }
    }

    public String getDescription(){
        if(descriptionGetter == null)
            return null;
        String language = LocaleContextHolder.getLocale().getLanguage();
        switch (language){
            case "pl": return descriptionGetter.getPl();
            default: return descriptionGetter.getEng();
        }
    }
}
