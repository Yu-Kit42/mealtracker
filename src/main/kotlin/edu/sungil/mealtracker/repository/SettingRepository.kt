package edu.sungil.mealtracker.repository

import edu.sungil.mealtracker.model.Setting
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SettingRepository : CrudRepository<Setting, Long> {
    
    fun findBySettingKey(settingKey: String): Setting?
    
    fun findBySettingKeyIn(settingKeys: List<String>): List<Setting>
}