package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class RuleNameServiceTests {
    
    @InjectMocks
    private RuleNameService ruleNameService;
    
    @Mock
    private RuleNameRepository ruleNameRepository;
    
    private RuleName ruleName1;
    private RuleName ruleName2;
    
    @BeforeEach
    public void setUp() {
        ruleName1 = new RuleName(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        ruleName2 = new RuleName(2, "Rule Name2", "Description2", "Json2", "Template2", "SQL2", "SQL Part2");
    }

    @Test
    public void getAllTest() {
        List<RuleName> ruleNames = Arrays.asList(ruleName1, ruleName2);
        when(ruleNameRepository.findAll()).thenReturn(ruleNames);
        List<RuleName> result = ruleNameService.getAll();
        verify(ruleNameRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void getByIdTest() {
        when(ruleNameRepository.findById(ruleName1.getId())).thenReturn(Optional.ofNullable(ruleName1));
        ruleNameService.createRuleName(ruleName1);
        RuleName result = ruleNameService.getById(ruleName1.getId());
        assertEquals(ruleName1, result);
        assertFalse(ObjectUtils.isEmpty(result));
        assertEquals(ruleName1.getName(), result.getName());
        assertEquals(ruleName1.getDescription(), result.getDescription());
        assertEquals(ruleName1.getJson(), result.getJson());
        assertEquals(ruleName1.getTemplate(), result.getTemplate());
        assertEquals(ruleName1.getSqlStr(), result.getSqlStr());
        assertEquals(ruleName1.getSqlPart(), result.getSqlPart());
    }

    @Test
    public void createRuleNameTest() {
        when(ruleNameRepository.save(ruleName1)).thenReturn(ruleName1);
        ruleNameService.createRuleName(ruleName1);
        verify(ruleNameRepository, times(1)).save(ruleName1);
    }

    @Test
    public void updateRuleNameTest() {
        when(ruleNameRepository.save(ruleName1)).thenReturn(ruleName1);
        ruleNameService.updateRuleName(ruleName1);
        verify(ruleNameRepository, times(1)).save(ruleName1);
    }

    @Test
    public void deleteRuleNameByIdTest() {
        when(ruleNameRepository.findById(ruleName1.getId())).thenReturn(Optional.ofNullable(ruleName1));
        ruleNameService.deleteRuleNameById(ruleName1.getId());
        verify(ruleNameRepository, times(1)).deleteById(ruleName1.getId());
    }
}
