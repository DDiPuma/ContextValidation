package edu.rutgers.contextvalidation;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

/**
 * Created by dominic on 11/14/17.
 */

@Dao
public interface ContextDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertContextFeatures(ContextFeatures features);
}
