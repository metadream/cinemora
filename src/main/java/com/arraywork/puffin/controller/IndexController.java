package com.arraywork.puffin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.arraywork.puffin.entity.User;
import com.arraywork.puffin.service.UserService;

import jakarta.annotation.Resource;

/**
 * Index Controller
 * @author AiChen
 * @created 2024/04/22
 */
@Controller
public class IndexController {

    @Resource
    private UserService userService;

    @GetMapping("/")
    public String index() {
        boolean hasSuperUser = userService.hasSuperUser();
        return hasSuperUser ? "index" : "redirect:init";
    }

    @GetMapping("/init")
    public String init() {
        return "init";
    }

    @PostMapping("/init")
    @ResponseBody
    public User init(@Validated @RequestBody User user) {
        user.setSuper(true);
        return userService.saveUser(user);
    }

}