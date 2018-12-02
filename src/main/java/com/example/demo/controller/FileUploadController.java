package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.ExcelLoadService;

@Controller
public class FileUploadController {

	@Autowired
	private ExcelLoadService service;

	@RequestMapping(path = "/uploaded", method = RequestMethod.GET)
	public String uploaded() {
		return "uploaded";
	}

	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file, Model model) {

		final Map<String, String> content = new HashMap<>();
		try {
			content.putAll(service.read(file.getInputStream()));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		model.addAttribute("content", content);

		return "uploaded";
	}

}
