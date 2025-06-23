package edu.sungil.mealtracker.service

import edu.sungil.mealtracker.model.MealRecord
import edu.sungil.mealtracker.repository.MealRecordRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@Service
class MealRecordService(
    private val mealRecordRepository: MealRecordRepository,
    private val settingService: SettingService
) {
    
    fun findById(id: Long): MealRecord? {
        return mealRecordRepository.findById(id).orElse(null)
    }
    
    fun findByUserIdAndDate(userId: Long, date: LocalDate): MealRecord? {
        return mealRecordRepository.findByUserIdAndMealDate(userId, date)
    }
    
    fun findByDate(date: LocalDate): List<MealRecord> {
        return mealRecordRepository.findByMealDate(date)
    }
    
    fun findByUserId(userId: Long): List<MealRecord> {
        return mealRecordRepository.findByUserId(userId)
    }
    
    fun findByDateRange(startDate: LocalDate, endDate: LocalDate): List<MealRecord> {
        return mealRecordRepository.findByDateRange(startDate, endDate)
    }
    
    fun findByUserIdAndDateRange(userId: Long, startDate: LocalDate, endDate: LocalDate): List<MealRecord> {
        return mealRecordRepository.findByUserIdAndDateRange(userId, startDate, endDate)
    }
    
    fun countEatingByDate(date: LocalDate): Int {
        return mealRecordRepository.countEatingByDate(date)
    }
    
    fun countEatingByDepartmentAndDate(date: LocalDate): Map<String, Int> {
        return mealRecordRepository.countEatingByDepartmentAndDate(date)
    }
    
    fun createOrUpdateMealRecord(mealRecord: MealRecord): MealRecord {
        val cutoffTime = getCutoffTime()
        val now = LocalTime.now()
        
        // Check if it's past the cutoff time for today's meal
        if (mealRecord.mealDate == LocalDate.now() && now.isAfter(cutoffTime)) {
            throw IllegalStateException("식사 신청 마감 시간이 지났습니다.")
        }
        
        // Check if the record already exists
        val existingRecord = mealRecordRepository.findByUserIdAndMealDate(mealRecord.userId, mealRecord.mealDate)
        
        return if (existingRecord != null) {
            // Update existing record
            val updatedRecord = existingRecord.copy(
                isEating = mealRecord.isEating,
                reason = mealRecord.reason,
                updatedAt = mealRecord.updatedAt
            )
            mealRecordRepository.save(updatedRecord)
        } else {
            // Create new record
            mealRecordRepository.save(mealRecord)
        }
    }
    
    fun deleteMealRecord(id: Long) {
        mealRecordRepository.deleteById(id)
    }
    
    fun getCurrentWeekDates(): List<LocalDate> {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (0..4).map { monday.plusDays(it.toLong()) } // Monday to Friday
    }
    
    fun getNextWeekDates(): List<LocalDate> {
        val today = LocalDate.now()
        val nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        return (0..4).map { nextMonday.plusDays(it.toLong()) } // Monday to Friday
    }
    
    private fun getCutoffTime(): LocalTime {
        val cutoffTimeStr = settingService.getMealCutoffTime()
        return LocalTime.parse(cutoffTimeStr)
    }
}