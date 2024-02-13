package com.blog.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.service.CommentService;
import com.blog.vo.Comment;
import com.blog.vo.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class CommentController {
	
	@Autowired
	CommentService commentService;
	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	@PostMapping("/comment")
	public Object savePost(HttpServletResponse response, @RequestBody Comment commentParam) {
		Comment comment = new Comment(commentParam.getPostId(), commentParam.getUser(), commentParam.getComment()); 
		boolean isSuccess = commentService.saveComment(comment);
		if(isSuccess) {
			return new Result(200, "Success");
		} else {
			response.setStatus (HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
			return new Result(500, "Fail");
		}
	}
	
	@GetMapping("/comments")
	public List<Comment> getComments(@RequestParam("post_id") Long postId){
		List<Comment> comments = commentService.getCommentList(postId);
		return comments;
	}
	
    
    @GetMapping("/comment")
	public Comment getComment(@RequestParam("id") Long id) {
		Comment comment = commentService.getComment(id);
		return comment;
	}
    
    @GetMapping("/comments/desc")
	public List<Comment> getCommentsOrderByRegDateDesc(@RequestParam("post_id") Long postId){
		List<Comment> comments = commentService.getCommentListOrderByRegDateDesc(postId);
		return comments;
	}
    
    @GetMapping("/comments/search")
    public List<Comment> searchComments(@RequestParam("post_id") Long postId, @RequestParam("query") String query) {
    	logger.info("Search Comments: Post ID = {}, Query = {}", postId, query);
    	List<Comment> comments = commentService.searchComments(postId, query);
    	return comments;
    }
    
    @DeleteMapping("/comment")
    public ResponseEntity<Object> deleteComment(@RequestParam("id") Long id) {
        boolean isSuccess = commentService.deleteComment(id);
        if (isSuccess) {
            return ResponseEntity.ok().body(new Result(200, "Comment deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Result(404, "Comment not found"));
        }
    }
	
}