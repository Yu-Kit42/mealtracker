package edu.sungil.mealtracker.service

import edu.sungil.mealtracker.model.Setting
import edu.sungil.mealtracker.repository.SettingRepository
import org.springframework.stereotype.Service

@Service
class SettingService(private val settingRepository: SettingRepository) {
    
    fun findById(id: Long): Setting? {
        return settingRepository.findById(id).orElse(null)
    }
    
    fun findByKey(key: String): Setting? {
        return settingRepository.findBySettingKey(key)
    }
    
    fun findByKeys(keys: List<String>): List<Setting> {
        return settingRepository.findBySettingKeyIn(keys)
    }
    
    fun findAll(): List<Setting> {
        return settingRepository.findAll().toList()
    }
    
    fun createSetting(setting: Setting): Setting {
        return settingRepository.save(setting)
    }
    
    fun updateSetting(setting: Setting): Setting {
        // Check if setting exists
        settingRepository.findById(setting.id ?: throw IllegalArgumentException("Setting ID cannot be null"))
            .orElseThrow { IllegalArgumentException("Setting not found") }
        
        return settingRepository.save(setting)
    }
    
    fun updateSettingValue(key: String, value: String): Setting {
        val setting = findByKey(key) ?: throw IllegalArgumentException("Setting with key $key not found")
        val updatedSetting = setting.copy(settingValue = value)
        return settingRepository.save(updatedSetting)
    }
    
    fun deleteSetting(id: Long) {
        settingRepository.deleteById(id)
    }
    
    fun getSchoolName(): String {
        return findByKey(Setting.KEY_SCHOOL_NAME)?.settingValue ?: "성일정보고등학교"
    }
    
    fun getSchoolLogoUrl(): String {
        return findByKey(Setting.KEY_SCHOOL_LOGO_URL)?.settingValue ?: "/images/logo.png"
    }
    
    fun getMealCutoffTime(): String {
        return findByKey(Setting.KEY_MEAL_CUTOFF_TIME)?.settingValue ?: "09:00:00"
    }
}