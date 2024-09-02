package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "Usertest", password = "passwordtest", roles = "USER")
public class RuleNameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    private RuleName ruleName1;
    private RuleName ruleName2;

    @BeforeEach
    public void setUp() {
        ruleName1 = new RuleName(1, "Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
        ruleName2 = new RuleName(2, "Rule Name2", "Description2", "Json2", "Template2", "SQL2", "SQL Part2");
    }

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("username", "Usertest"))
                .andExpect(view().name("ruleName/list"));
    }

    @Test
    public void addRuleNameTest() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    public void validateTest() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", ruleName1.getName())
                        .param("description", ruleName1.getDescription())
                        .param("json", ruleName1.getJson())
                        .param("template", ruleName1.getTemplate())
                        .param("sqlStr", ruleName1.getSqlStr())
                        .param("sqlPart", ruleName1.getSqlPart())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
        verify(ruleNameService, times(1)).createRuleName(any(RuleName.class));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        when(ruleNameService.getById(1)).thenReturn(ruleName1);
        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("ruleName/update"));
        verify(ruleNameService, times(1)).getById(1);
    }

    @Test
    public void showUpdateFormWhenNotFoundTest() throws Exception {
        when(ruleNameService.getById(1)).thenThrow(new IllegalArgumentException("No ruleName found."));
        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("ruleName/update"));
        verify(ruleNameService, times(1)).getById(1);
    }

    @Test
    public void updateRuleNameTest() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .param("name", ruleName2.getName())
                        .param("description", ruleName2.getDescription())
                        .param("json", ruleName2.getJson())
                        .param("template", ruleName2.getTemplate())
                        .param("sqlStr", ruleName2.getSqlStr())
                        .param("sqlPart", ruleName2.getSqlPart())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    public void deleteRuleNameTest() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}
