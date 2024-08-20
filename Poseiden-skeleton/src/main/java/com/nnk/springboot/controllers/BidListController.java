package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;


@Controller
public class BidListController {

    @Autowired
    private BidListService bidListService;

    @GetMapping("/bidList/list")
    public String home(Model model) {
        List<BidList> bidLists = bidListService.getAll();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("bidLists", bidLists);
        model.addAttribute("username", username);
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidListForm(Model model) {
        model.addAttribute("bidList", new BidList());
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result) {
        if(result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.createBidList(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidList = bidListService.getById(id);
        model.addAttribute("bidList", bidList);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBidList(@PathVariable("id") Integer id, @Valid BidList bidList, BindingResult result) {
        if(result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.updateBidList(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBidList(@PathVariable("id") Integer id) {
        bidListService.deleteBidListById(id);
        return "redirect:/bidList/list";
    }
}
