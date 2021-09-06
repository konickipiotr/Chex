package com.chex.config;

import java.io.File;

public class GlobalSettings {
    public static final String appRootPaht = System.getProperty("user.home") + File.separator + "chex";
    public static final String usersSpace = appRootPaht + File.separator + "users";


    public final static long USER_ACCOUNT_REMOVED= -99;
}
