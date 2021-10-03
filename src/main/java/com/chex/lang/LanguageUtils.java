package com.chex.lang;

import com.chex.modules.places.repository.PlaceDescriptionRepository;
import com.chex.modules.places.repository.PlaceNameRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.repository.JpaRepository;

public class LanguageUtils {

    private LanguageGetter nameGetter;
    private LanguageGetter descriptionGetter;

    public LanguageUtils(LanguageGetter nameGetter, LanguageGetter descriptionGetter) {
        this.nameGetter = nameGetter;
        this.descriptionGetter = descriptionGetter;
    }

    public LanguageUtils(LanguageGetter nameGetter) {
        this.nameGetter = nameGetter;
    }

    public String getName(){
        String language = LocaleContextHolder.getLocale().getLanguage();
        switch (language){
            case "pl": return nameGetter.getPl();
            default: return nameGetter.getEng();
        }
    }

    public String getDescription(){
        String language = LocaleContextHolder.getLocale().getLanguage();
        switch (language){
            case "pl": return descriptionGetter.getPl();
            default: return descriptionGetter.getEng();
        }
    }
}
