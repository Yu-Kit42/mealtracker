package edu.sungil.mealtracker.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("USERS")
data class User(
    @Id
    val id: Long? = null,
    val username: String,
    val password: String,
    val name: String,
    val role: String,
    val department: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        const val ROLE_TEACHER = "TEACHER"
        const val ROLE_ADMIN = "ADMIN"
    }
}