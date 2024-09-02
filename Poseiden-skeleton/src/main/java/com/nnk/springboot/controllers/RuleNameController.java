package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

import java.util.List;

@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> ruleNames = ruleNameService.getAll();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("ruleNames", ruleNames);
        model.addAttribute("username", username);
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleNameForm(Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName) {
        ruleNameService.createRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            RuleName ruleName = ruleNameService.getById(id);
            model.addAttribute("ruleName", ruleName);
        }
         catch (IllegalArgumentException e) {
             model.addAttribute("errorMessage", e.getMessage());
             model.addAttribute("ruleName", new RuleName());
         }
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName) {
        ruleNameService.updateRuleName(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteRuleNameById(id);
        return "redirect:/ruleName/list";
    }
}
