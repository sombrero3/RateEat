package com.example.rateeat.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rateeat.MyApplication;


@Database(entities = {Review.class,}, version = 6)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract ReviewDao reviewDao();
}
public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
