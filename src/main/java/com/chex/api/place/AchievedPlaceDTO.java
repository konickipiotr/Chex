package com.chex.api.place;

import com.chex.api.post.PostVisibility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievedPlaceDTO {

    private Map<String, Integer> achievedPlaces;
    private LocalDateTime timestamp;
    private String description;
    private List<String> sfiles;
    private PostVisibility postvisibility = PostVisibility.PUBLIC;

    public AchievedPlaceDTO() {
    }

    public Map<String, Integer> getAchievedPlaces() {
        return achievedPlaces;
    }

    public void setAchievedPlaces(Map<String, Integer> achievedPlaces) {
        this.achievedPlaces = achievedPlaces;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSfiles() {
        return sfiles;
    }

    public void setSfiles(List<String> sfiles) {
        this.sfiles = sfiles;
    }

    public PostVisibility getPostvisibility() {
        return postvisibility;
    }

    public void setPostvisibility(PostVisibility postvisibility) {
        this.postvisibility = postvisibility;
    }
}
