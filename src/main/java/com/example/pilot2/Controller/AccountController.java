package com.example.pilot2.Controller;

import com.example.pilot2.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

//    @GetMapping("/accounts/list/admin")
//    public
}
