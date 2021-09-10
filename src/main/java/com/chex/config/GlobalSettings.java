package com.chex.config;

import java.io.File;

public class GlobalSettings {
    public static final String domain = "http://localhost:8080";
    //public static final String domain = "http://80.211.245.217:8080";

    public static final String appRootPaht = System.getProperty("user.home") + File.separator + "chex";
    public static final String usersSpace = appRootPaht + File.separator + "users";

    public final static String appPath = "/home/piterk/chex/";


    public final static long USER_ACCOUNT_REMOVED= -99;
}
