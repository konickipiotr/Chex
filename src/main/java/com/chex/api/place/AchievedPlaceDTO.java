package com.chex.api.place;

import com.chex.api.post.PostVisibility;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@ToString
public class AchievedPlaceDTO {

    private Map<String, Integer> achievedPlaces;
    private LocalDateTime timestamp;
    private String description;
    private List<String> sfiles;
    private PostVisibility postvisibility = PostVisibility.PUBLIC;
}
