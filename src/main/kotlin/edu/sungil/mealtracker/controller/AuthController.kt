package edu.sungil.mealtracker.controller

import edu.sungil.mealtracker.model.User
import edu.sungil.mealtracker.service.SettingService
import edu.sungil.mealtracker.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AuthController(
    private val userService: UserService,
    private val settingService: SettingService
) {
    
    @GetMapping("/login")
    fun loginPage(model: Model): String {
        model.addAttribute("schoolName", settingService.getSchoolName())
        model.addAttribute("schoolLogoUrl", settingService.getSchoolLogoUrl())
        return "auth/login"
    }
    
    @PostMapping("/login")
    fun login(
        @RequestParam username: String,
        @RequestParam password: String,
        request: HttpServletRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        val user = userService.authenticate(username, password)
        
        return if (user != null) {
            // Create authentication token
            val authorities = listOf(SimpleGrantedAuthority(user.role))
            val authentication = UsernamePasswordAuthenticationToken(user, null, authorities)
            
            // Set authentication in security context
            val securityContext = SecurityContextHolder.getContext()
            securityContext.authentication = authentication
            
            // Create a new session and add the security context
            val session = request.getSession(true)
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext)
            
            // Store user in session
            session.setAttribute("user", user)
            
            // Redirect based on role
            if (user.role == User.ROLE_ADMIN) {
                "redirect:/admin/dashboard"
            } else {
                "redirect:/meal-dashboard"
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.")
            "redirect:/login"
        }
    }
    
    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): String {
        val session = request.getSession(false)
        if (session != null) {
            session.invalidate()
        }
        SecurityContextHolder.clearContext()
        return "redirect:/login?logout"
    }
    
    @GetMapping("/register")
    fun registerPage(model: Model): String {
        model.addAttribute("schoolName", settingService.getSchoolName())
        model.addAttribute("schoolLogoUrl", settingService.getSchoolLogoUrl())
        return "auth/register"
    }
    
    @PostMapping("/register")
    fun register(
        @RequestParam username: String,
        @RequestParam password: String,
        @RequestParam name: String,
        @RequestParam department: String,
        redirectAttributes: RedirectAttributes
    ): String {
        // Check if username already exists
        if (userService.findByUsername(username) != null) {
            redirectAttributes.addFlashAttribute("error", "이미 사용 중인 아이디입니다.")
            return "redirect:/register"
        }
        
        // Create new user
        val user = User(
            username = username,
            password = password,
            name = name,
            role = User.ROLE_TEACHER,
            department = department
        )
        
        userService.createUser(user)
        redirectAttributes.addFlashAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.")
        return "redirect:/login"
    }
}