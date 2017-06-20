package com.orsomob.coordinates.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by LucasOrso on 6/4/17.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "Coordinates";

    public static final int VERSION = 1;
}