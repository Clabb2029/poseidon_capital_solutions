package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public List<RuleName> getAll() {
        return ruleNameRepository.findAll();
    }

    public RuleName getById(Integer id) {
        return ruleNameRepository.findById(id).get();
    }

    public void createRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public void updateRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    public void deleteRuleNameById(Integer id) {
        ruleNameRepository.deleteById(id);
    }
}
