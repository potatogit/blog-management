package com.waylau.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.service.UserService;
import com.waylau.spring.boot.blog.vo.Response;

@Controller
@RequestMapping("/u")
public class UserspaceController {

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	UserService userService;
 
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username) {
		System.out.println("username" + username);
		return "u";
	}
 
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="category",required=false ) Long category,
			@RequestParam(value="keyword",required=false ) String keyword) {
		
		if (category != null) {
			
			System.out.print("category:" +category );
			System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?category="+category);
			return "/u";
			
		} else if (keyword != null && keyword.isEmpty() == false) {
			
			System.out.print("keyword:" +keyword );
			System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?keyword="+keyword);
			return "/u";
		}  
		
		System.out.print("order:" +order);
		System.out.print("selflink:" + "redirect:/u/"+ username +"/blogs?order="+order);
		return "/u";
	}
	
	@GetMapping("/{username}/blogs/{id}")
	public String listBlogsByOrder(@PathVariable("id") Long id) {
		 
		System.out.print("blogId:" + id);
		return "/blog";
	}
	
	
	@GetMapping("/{username}/blogs/edit")
	public String editBlog() {
 
		return "/blogedit";
	}

	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView profile(@PathVariable("username") String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/profile", "userModel", model);
	}

	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
		User originalUser = userService.getUserById(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setUsername(user.getUsername());

		String oldPw = originalUser.getPassword();
		String newPw = user.getPassword();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPw = passwordEncoder.encode(newPw);
		boolean isMatch = passwordEncoder.matches(oldPw, encodedPw);
		if(!isMatch) {
			originalUser.setEncodePassword(encodedPw);
		}

		userService.saveUser(originalUser);
		return "redirect:/u/" + username +"/profile";
	}

	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("/userspace/avatar", "userModel", model);
	}

	@PostMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
		String url = user.getAvatar();
		User curUser = userService.getUserById(user.getId());
		curUser.setAvatar(url);
		userService.saveUser(curUser);

		return ResponseEntity.ok().body(new Response(true, "处理成功", url));
	}
}
