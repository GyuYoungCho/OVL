package com.project.ovl.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
	private static final String SUCCESS = "success";
}
