package com.example.rateeat.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReviewDao {
    @Query("select * from Review")
    List<Review> getAll();

    @Query("select * from Review where  id =:reviewId LIMIT 1")
    Review getReviewById(String reviewId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Review... reviews);

    @Delete
    void delete(Review review);
}
