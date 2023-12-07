package com.dcs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcs.dto.Resume;
import com.dcs.dto.Role;
import com.dcs.dto.User;
import com.dcs.service.IResumeServiceImpl;
import com.dcs.service.IRoleServiceImpl;
import com.dcs.service.IUserServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/users")
public class UserController {
	
	
	@Autowired
	private IUserServiceImpl userServiceImpl;
	@Autowired
	private IResumeServiceImpl resumeServiceImpl;
	@Autowired
	private IRoleServiceImpl roleServiceImpl;

	/*ROLE ADMIN 
	  Lista a todos los usuarios*/
	@GetMapping("/all")
	public List<User> listarUsers(){
		return userServiceImpl.listUsers();
	}

	/*ROLE ADMIN 
	  Ver usuario por id*/
	@GetMapping("/{id}")
	public User userXID(@PathVariable(name="id") Integer id) {
		User user_xid= new User();	
		user_xid=userServiceImpl.userById(id);
		return user_xid;
	}
	
	/*ROLE USER 
	  Ver informacion del usuario*/
	@GetMapping("/current_user/info")
	public User userInfo() {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
	    System.out.println("GET NAME"+authentication.getName());
	    User current_user = userServiceImpl.findByEmail(authentication.getName());
	    return userServiceImpl.userById(current_user.getId());
		
	}
	
	/*ROLE ADMIN 
	  Añadir foto de perfil*/
	@PutMapping("/photo")
	public ResponseEntity add_photo(@RequestBody byte[] photo) {
		
		try {
			System.out.println("TEST PHOTO");
		    org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
		    System.out.println("GET NAME"+authentication.getName());
		    User current_user = userServiceImpl.findByEmail(authentication.getName());
		    //User current_user = userServiceImpl.findByEmail("test@test.com");
		    System.out.println("EMAIL USER :"+current_user.getEmail());
			if (photo != null) {
		        current_user.setPhoto(photo);
		        userServiceImpl.saveUser(current_user);
		    }
	        return ResponseEntity.ok("Photo added successfully");
	    } catch (Exception e) {
	        e.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding photo");
	    }
	}
	
	/*ROLE ADMIN 
	  Añadir curriculum*/
	@PutMapping("/resume")
	public ResponseEntity add_resume(@RequestBody byte[] resume) {
		
		try {
			System.out.println("TEST RESUME");
		    org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    User current_user = userServiceImpl.findByEmail(authentication.getName());
		    //User current_user = userServiceImpl.findByEmail("test@test.com");
		    System.out.println("EMAIL USER :"+current_user.getEmail());
			if (resume != null) {
				Resume r = new Resume();
				r.setId_user(current_user.getId());
				r.setResume(resume);	
		        resumeServiceImpl.saveResume(r);
		        current_user.setResume(r);  
		        userServiceImpl.saveUser(current_user);
		    }
	        return ResponseEntity.ok("Resume added successfully");
	    } catch (Exception e) {
	        e.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding photo");
	    }	
	}
	
	/*ROLE ADMIN 
	  Cambiar el rol a un usuario*/
	@PutMapping("/change_role/{id_user}/{role}")
	public ResponseEntity actualizarRol(@PathVariable(name="id_user") Integer id,@PathVariable(name="role") String role) {
		try {
			User user = userServiceImpl.userById(id);
			Role role_user = roleServiceImpl.findByName(role); 
			user.setRole(role_user);
			
			userServiceImpl.saveUser(user);
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding photo");
		}
	
        return ResponseEntity.ok("Role changed successfully");
		
	}
	
	/*ROLE ADMIN 
	  Borrar un usuario por id*/
	@DeleteMapping("/deleteUser/{id}")
	public void eliminaruser(@PathVariable(name="id")Integer id) {
		userServiceImpl.deleteByIdUser(id);
	}
	
}