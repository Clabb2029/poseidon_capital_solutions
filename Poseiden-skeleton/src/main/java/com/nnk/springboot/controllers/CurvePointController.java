package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;

@Controller
public class CurvePointController {

    @Autowired
    private CurvePointService curvePointService;

    @GetMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> curvePoints = curvePointService.getAll();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("curvePoints", curvePoints);
        model.addAttribute("username", username);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(Model model) {
        model.addAttribute("curvePoint", new CurvePoint());
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {
        if(result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.createCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.getById(id);
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint, BindingResult result) {
        if(result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePointById(id);
        return "redirect:/curvePoint/list";
    }
}
