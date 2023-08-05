package com.c9pay.gateway.utils;

import java.util.Arrays;
import java.util.List;

public class UriParser {
    public static boolean parse(String uri, String pattern){
        List<String> splits = Arrays.stream(uri.split("/")).toList();
        for (String split : splits) {
            if(split.equals(pattern)) return true;
        }
        return false;
    }

}
