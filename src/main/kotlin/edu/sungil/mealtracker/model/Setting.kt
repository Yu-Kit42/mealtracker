package edu.sungil.mealtracker.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("SETTINGS")
data class Setting(

    @Id
    val id: Long? = null,
    val settingKey: String,
    val settingValue: String,
    val description: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        const val KEY_MEAL_CUTOFF_TIME = "MEAL_CUTOFF_TIME"
        const val KEY_SCHOOL_NAME = "SCHOOL_NAME"
        const val KEY_SCHOOL_LOGO_URL = "SCHOOL_LOGO_URL"
    }
}