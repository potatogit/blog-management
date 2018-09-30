package com.waylau.spring.boot.blog.controller;

import java.util.List;

import javax.naming.ldap.Control;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.waylau.spring.boot.blog.domain.Blog;
import com.waylau.spring.boot.blog.domain.Catalog;
import com.waylau.spring.boot.blog.domain.User;
import com.waylau.spring.boot.blog.domain.Vote;
import com.waylau.spring.boot.blog.service.BlogService;
import com.waylau.spring.boot.blog.service.CatalogService;
import com.waylau.spring.boot.blog.service.UserService;
import com.waylau.spring.boot.blog.util.ConstraintViolationExceptionHandler;
import com.waylau.spring.boot.blog.util.ControllerHelper;
import com.waylau.spring.boot.blog.vo.Response;

@Controller
@RequestMapping("/u")
public class UserspaceController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
	    User user = (User)userDetailsService.loadUserByUsername(username);
	    model.addAttribute("user", user);
		return "redirect:/u/" + username + "/blogs";
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


	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="catalog",required=false ) Long catalogId,
			@RequestParam(value="keyword",required=false, defaultValue = "") String keyword,
			@RequestParam(value="async",required=false ) boolean async,
			@RequestParam(value="pageIndex",required=false, defaultValue = "0") int pageIndex,
			@RequestParam(value="pageSize",required=false, defaultValue = "10") int pageSize,
			Model model
	) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);

		Page<Blog> page = null;
		if(catalogId !=null && catalogId > 0) {
			Catalog catalog = catalogService.getCatalogById(catalogId);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByCatalog(catalog, pageable);
			order = "";
		} else if(order.equals("hot")) {
			Sort sort = new Sort(Sort.Direction.DESC, "readingSize", "commentSize", "voteSize");
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
		} else if (order.equals("new")) {
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByTitleLike(user, keyword, pageable);
		}

		List<Blog> list = page.getContent();
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		return async ? "/userspace/u :: #mainContainerReplace" : "/userspace/u";
	}

	@GetMapping("/{username}/blogs/{id}")
	public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		blogService.readingIncrease(id);
		String currentUserName = ControllerHelper.getCurrentUserName();
		boolean isBlogOwner = username.equals(currentUserName);
		Blog blog = blogService.getBlogById(id);
		Vote vote = blog.getVotes().stream()
				.filter(item -> item.getUser().getName().equals(currentUserName))
				.findAny()
				.orElse(null);

		model.addAttribute("currentVote", vote);
		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("blogModel", blogService.getBlogById(id));
		return "/userspace/blog";
	}

	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", new Blog(null, null, null));
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}

	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id) {
		try{
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}

	@GetMapping("/{username}/blogs/edit/{id}")
	public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		User user = (User)userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", blogService.getBlogById(id));
		return new ModelAndView("/userspace/blogedit", "blogModel", model);
	}

	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
		// is this a must?
//		if (blog.getCatalog().getId() == null) {
//			return ResponseEntity.ok().body(new Response(false,"未选择分类"));
//		}
		User user = (User)userDetailsService.loadUserByUsername(username);
		blog.setUser(user);
		try{
			if(blog.getId() != null) {
				Blog originalBlog = blogService.getBlogById(blog.getId());
				originalBlog.setTitle(blog.getTitle());
				originalBlog.setContent(blog.getContent());
				originalBlog.setSummary(blog.getSummary());
				originalBlog.setCatalog(blog.getCatalog());
				originalBlog.setTags(blog.getTags());
				blogService.saveBlog(blog);
			} else {
				blog.setUser(user);
				blogService.saveBlog(blog);
			}
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}

}
