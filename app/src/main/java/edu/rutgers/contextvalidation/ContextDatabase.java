package edu.rutgers.contextvalidation;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by dominic on 11/14/17.
 */

@Database(entities = ContextFeatures.class, version = 1)
public abstract class ContextDatabase extends RoomDatabase {
    public abstract ContextDao contextDao();

}
