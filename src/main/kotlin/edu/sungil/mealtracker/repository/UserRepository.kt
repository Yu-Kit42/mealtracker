package edu.sungil.mealtracker.repository

import edu.sungil.mealtracker.model.User
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    
    fun findByUsername(username: String): User?
    
    @Query("SELECT * FROM users WHERE role = :role")
    fun findByRole(@Param("role") role: String): List<User>
    
    @Query("SELECT * FROM users WHERE department = :department")
    fun findByDepartment(@Param("department") department: String): List<User>
    
    @Query("SELECT DISTINCT department FROM users WHERE department IS NOT NULL")
    fun findAllDepartments(): List<String>
}