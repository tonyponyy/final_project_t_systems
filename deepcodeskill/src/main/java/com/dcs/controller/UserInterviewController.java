package com.dcs.controller;

import java.util.Date;
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

import com.dcs.dto.Interview;
import com.dcs.dto.InterviewSkill;
import com.dcs.dto.Skill;
import com.dcs.dto.User;
import com.dcs.dto.UserInterview;
import com.dcs.service.IInterviewServiceImpl;
import com.dcs.service.IUserInterviewServiceImpl;
import com.dcs.service.IUserServiceImpl;

@RestController
@RequestMapping("/userinterviews")
public class UserInterviewController {
	
	@Autowired
	IUserInterviewServiceImpl uiSer;
	
	@Autowired
	IInterviewServiceImpl iSer;
	
	@Autowired
	private IUserServiceImpl userServiceImpl;
	
	@PostMapping("/user_join_interview/{id_interview}")
	public ResponseEntity<UserInterview> addUserToInterview(@PathVariable(name="id_interview") Integer id_interview) {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
	    System.out.println("GET NAME"+authentication.getName());
	    User current_user = userServiceImpl.findByEmail(authentication.getName());
		Interview interview =iSer.listById(id_interview);
		
		UserInterview u1 = new UserInterview();
		u1.setUser(current_user);
		u1.setInterview(interview);
		u1.setState(1);
		u1.setJoined_at(new Date());
		
		return new ResponseEntity<>(uiSer.saveUserInterview(u1), HttpStatus.OK);
	}
	
	@GetMapping("/user_interviews")
	public ResponseEntity<List<UserInterview>> userListInterviews(){
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); 
	    System.out.println("GET NAME"+authentication.getName());
	    User current_user = userServiceImpl.findByEmail(authentication.getName());
	    List<UserInterview> userInterviews = uiSer.findByUser(current_user);
		return new ResponseEntity<>(userInterviews, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public List<UserInterview> listUserInterview(){
		return uiSer.listUserInterview();
	}
	
	@GetMapping("/{id}")
	public UserInterview listUserInterviewById(@PathVariable(name="id") Integer id) {
		return uiSer.listUserInterviewById(id);
	}
	
	@PostMapping("/add")
	public UserInterview saveUserInterview(@RequestBody UserInterview ui) {
		return uiSer.saveUserInterview(ui);
	}
	
	@PutMapping("/{id}")
	public UserInterview updateUserInterview(@PathVariable(name="id") Integer id, @RequestBody UserInterview ui) {
		UserInterview ui1 = uiSer.listUserInterviewById(id);
		UserInterview ui2 = new UserInterview();
		
		ui1.setId(ui.getId());
		ui1.setInterview(ui.getInterview());
		ui1.setUser(ui.getUser());
		ui1.setInternal_comment(ui.getInternal_comment());
		ui1.setJoined_at(ui.getJoined_at());
		ui1.setStamp(ui.getStamp());
		ui1.setState(ui.getState());
		
		ui2 = uiSer.updateUserInterview(ui1);
		
		return ui2;
	}
	
	@DeleteMapping("/{id}")
	public void deleteByIdUserInterview(@PathVariable(name="id") Integer id) {
		uiSer.deleteByIdUserInterview(id);
	}
	

}
