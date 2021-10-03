package com.chex.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    public static String idsToString(List<Long> ids){
        List<String> places = new ArrayList<>();
        for(Long s : ids){
            String num = Long.toString(s);
            places.add(Long.toString(s));
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

    public static List<String> stringIdsToList(String ids){
        if(ids == null || ids.isBlank()) return new ArrayList<>();
        return Arrays.asList(ids.split(":"));
    }

    public static List<Long> idsToList(String ids){
        if(ids == null || ids.isBlank()) return new ArrayList<>();
        String[] split = ids.split(":");
        return Arrays.stream(split).map(i -> Long.parseLong(i)).collect(Collectors.toList());
    }


    public static String extractId(String id){
        String[] splited = id.split("\\.");

        splited[4] ="00000";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < splited.length; i++){
            if(splited[i].equals("000") || splited[i].equals("00000"))
                continue;

            if(i == 0)
                sb.append(splited[i]);
            else
                sb.append("." + splited[i]);
        }

        return sb.toString();
    }

}
