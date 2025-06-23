package edu.sungil.mealtracker.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("MEAL_RECORDS")
data class MealRecord(
    @Id
    val id: Long? = null,
    val userId: Long,
    val mealDate: LocalDate,
    val isEating: Boolean = false,
    val reason: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)