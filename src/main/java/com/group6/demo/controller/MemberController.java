package com.group6.demo.controller;

import com.group6.demo.entity.login.Member;
import com.group6.demo.entity.login.MemberDTO;
import com.group6.demo.security.SignUpFormValidator;
import com.group6.demo.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@Slf4j
public class MemberController {
    //private static final Logger logger =
    //LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService ms;

    @Autowired
    private SignUpFormValidator signUpFormValidator;

    // 홈
    @GetMapping("/home")
    public String home() {
        return "/index";
    }

    // 로그인폼
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "/login";
    }

	/*
	@GetMapping("modifyForm")
	public String modify_form() {
		return "/modify";
	}
	*/

    // 비밀번호 수정 -> 수정폼인데 (아직x)
    @PostMapping("/modify")
    public String modify(MemberDTO newMemberDTO, Authentication authentication) throws Exception {
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member member = Member.builder()
                .name(memberDTO.getName())
                .account(memberDTO.getAccount())
                .password(passwordEncoder.encode(newMemberDTO.getPassword()))
                .email(newMemberDTO.getEmail())
                .build();

        return "redirect:/home";
    }

    // 마이페이지 비밀번호 변경
    @PostMapping("/changePw")
    public String changePw(@RequestParam String account, String newPassword) {
        log.info("ACCOUNT : " + account + ", NEWPASSWORD : " + newPassword);
        ms.changePasswordByAccount(account, newPassword);
        return "";
    }

    // 이름으로 찾기
    @PostMapping("findMemberByName")
    public String findMemberByName(@RequestParam String name) {
        log.info("NAME : " + name);
        ms.findMemberByName(name);
        return "";
    }


    // 회원가입
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "/saveView";
    }

    // 회원가입 완료
    @PostMapping("/register")
    public String register(@Valid MemberDTO dto, Errors errors, RedirectAttributes attributes) {

        if (errors.hasErrors()) {
            return "register";
        }

        attributes.addFlashAttribute("message", "회원가입 성공");
        Long result = ms.save(dto);

        log.info("register : " + dto);
        return "redirect:/home";

    }

    // 마이페이지 수정폼
    @GetMapping("/mypage")
    public String mypage() {

        return "/modify";
    }

    @InitBinder("register")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/username")
    public String currentUserName(Principal principal, Model model) {
        String name = principal.getName();

        model.addAttribute("name", name);
        return "username";
    }

    @GetMapping("/username1")
    public String currentUserName(Authentication  authentication, Model model) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String name = userDetails.getUsername();

        model.addAttribute("name", name);
        return "username1";
    }


//마이페이지
//   @GetMapping("items/{account}/mypage")
//   public String updateItemForm(@PathVariable("account") Long account,
//   		Model model) {
//
//   }
}
