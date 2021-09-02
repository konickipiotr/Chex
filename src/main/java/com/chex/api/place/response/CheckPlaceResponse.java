package com.chex.api.place.response;

import com.chex.modules.places.CheckPlaceView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckPlaceResponse {
    private CheckPlaceResponseStatus responseStatus;
    private List<CheckPlaceView> checkPlaceViewList;
}
