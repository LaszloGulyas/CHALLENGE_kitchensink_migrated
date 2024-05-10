package com.laszlogulyas.kitchensink_migrated.controller;

import com.laszlogulyas.kitchensink_migrated.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/kitchensink")
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberController memberController;

    @GetMapping
    public String listAllMembers(Model model) {
        addHomeFields(model);
        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("newMember") Member newMember, Model model, BindingResult bindingResult) {

        ResponseEntity<Map<String, String>> response = memberController.createMember(newMember);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/kitchensink";
        } else {
            addErrorsFromMap(Objects.requireNonNull(response.getBody()), bindingResult);
            model.addAttribute("errors", bindingResult.getFieldErrors());
            addHomeFields(model);
            return "index";
        }
    }

    private void addHomeFields(Model model) {
        List<Member> members = memberController.listAllMembers();
        model.addAttribute("members", members);
        if (!model.containsAttribute("newMember")) {
            model.addAttribute("newMember", new Member());
        }
    }

    private void addErrorsFromMap(Map<String, String> errorMap, BindingResult bindingResult) {
        errorMap.entrySet().stream()
                .map(entry -> new FieldError(bindingResult.getObjectName(), entry.getKey(), entry.getValue()))
                .forEach(bindingResult::addError);
    }
}
