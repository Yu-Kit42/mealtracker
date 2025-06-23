package edu.sungil.mealtracker.service

import edu.sungil.mealtracker.model.User
import edu.sungil.mealtracker.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    
    fun findById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }
    
    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
    
    fun findAllTeachers(): List<User> {
        return userRepository.findByRole(User.ROLE_TEACHER)
    }
    
    fun findAllAdmins(): List<User> {
        return userRepository.findByRole(User.ROLE_ADMIN)
    }
    
    fun findByDepartment(department: String): List<User> {
        return userRepository.findByDepartment(department)
    }
    
    fun findAllDepartments(): List<String> {
        return userRepository.findAllDepartments()
    }
    
    fun findAll(): List<User> {
        return userRepository.findAll().toList()
    }
    
    fun createUser(user: User): User {
        val encodedPassword = passwordEncoder.encode(user.password)
        val userWithEncodedPassword = user.copy(password = encodedPassword)
        return userRepository.save(userWithEncodedPassword)
    }
    
    fun updateUser(user: User): User {
        // Check if user exists
        val existingUser = userRepository.findById(user.id ?: throw IllegalArgumentException("User ID cannot be null"))
            .orElseThrow { IllegalArgumentException("User not found") }
        
        // If password is unchanged (same as encoded password in DB), don't re-encode it
        val updatedUser = if (user.password == existingUser.password) {
            user
        } else {
            user.copy(password = passwordEncoder.encode(user.password))
        }
        
        return userRepository.save(updatedUser)
    }
    
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
    
    fun authenticate(username: String, password: String): User? {
        val user = userRepository.findByUsername(username) ?: return null
        return if (passwordEncoder.matches(password, user.password)) user else null
    }
}