package com.chex.webapp.admin.achievements;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AchievementForm {
    private String namepl;
    private String nameen;
    private int points;
    private String descriptionen;
    private String descriptionpl;
    private String[] achievementPlaces;
    private MultipartFile picture;
}
