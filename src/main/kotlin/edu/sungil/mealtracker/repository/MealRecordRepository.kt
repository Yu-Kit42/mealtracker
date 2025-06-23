package edu.sungil.mealtracker.repository

import edu.sungil.mealtracker.model.MealRecord
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface MealRecordRepository : CrudRepository<MealRecord, Long> {
    
    fun findByUserIdAndMealDate(userId: Long, mealDate: LocalDate): MealRecord?
    
    fun findByMealDate(mealDate: LocalDate): List<MealRecord>
    
    fun findByUserId(userId: Long): List<MealRecord>
    
    @Query("SELECT * FROM meal_records WHERE meal_date BETWEEN :startDate AND :endDate")
    fun findByDateRange(
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<MealRecord>
    
    @Query("SELECT * FROM meal_records WHERE user_id = :userId AND meal_date BETWEEN :startDate AND :endDate")
    fun findByUserIdAndDateRange(
        @Param("userId") userId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<MealRecord>
    
    @Query("SELECT COUNT(*) FROM meal_records WHERE meal_date = :date AND is_eating = true")
    fun countEatingByDate(@Param("date") date: LocalDate): Int
    
    @Query("""
        SELECT u.department, COUNT(*) as count 
        FROM meal_records mr 
        JOIN users u ON mr.user_id = u.id 
        WHERE mr.meal_date = :date AND mr.is_eating = true AND u.department IS NOT NULL 
        GROUP BY u.department
    """)
    fun countEatingByDepartmentAndDate(@Param("date") date: LocalDate): Map<String, Int>
}