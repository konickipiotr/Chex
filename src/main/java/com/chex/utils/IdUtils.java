package com.chex.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class IdUtils {
    public static String placeIdsToString(Set<String> ids){
        List<String> places = new ArrayList<>();
        for(String s : ids){
            if(s.endsWith(".00000"))
                continue;
            places.add(s);
        }

        Collections.sort(places);
        return StringUtils.join(places, ":");
    }

    public static String subplaceIdsToString(Set<String> ids){
        List<String> places = new ArrayList<>();
        for(String s : ids){
            if(s.endsWith(".00000"))
                places.add(s);
        }
        Collections.sort(places);
        return StringUtils.join(places, ":");
    }
}
