package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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

import java.sql.Timestamp;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "Usertest", password = "passwordtest", roles = "USER")
public class CurvePointControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    private CurvePoint curvePoint1;
    private CurvePoint curvePoint2;

    @BeforeEach
    public void setUp() {
        curvePoint1 = new CurvePoint(10, 1, new Timestamp(new Date().getTime()), 3d, 4d, new Timestamp(new Date().getTime()));
        curvePoint2 = new CurvePoint(20, 2, new Timestamp(new Date().getTime()), 5d, 6d, new Timestamp(new Date().getTime()));
    }

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("username", "Usertest"))
                .andExpect(view().name("curvePoint/list"));
    }

    @Test
    public void addCurvePointTest() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    public void validateTest() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", String.valueOf(curvePoint1.getCurveId()))
                        .param("term", String.valueOf(curvePoint1.getTerm()))
                        .param("value", String.valueOf(curvePoint1.getValue()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        verify(curvePointService, times(1)).createCurvePoint(any(CurvePoint.class));
    }

    @Test
    public void validateWhenFormNotValidTest() throws Exception {
        mockMvc.perform(post("/curvePoint/validate"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
        verify(curvePointService, times(0)).createCurvePoint(any(CurvePoint.class));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        when(curvePointService.getById(1)).thenReturn(curvePoint1);
        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("curvePoint/update"));
        verify(curvePointService, times(1)).getById(1);
    }

    @Test
    public void showUpdateFormWhenNotFoundTest() throws Exception {
        when(curvePointService.getById(1)).thenThrow(new IllegalArgumentException("No curvePoint found."));
        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("curvePoint/update"));
        verify(curvePointService, times(1)).getById(1);
    }

    @Test
    public void updateCurvePointTest() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", String.valueOf(curvePoint2.getCurveId()))
                        .param("term", String.valueOf(curvePoint2.getTerm()))
                        .param("value", String.valueOf(curvePoint2.getValue()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    public void updateCurvePointWhenFormNotValidTest() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
        verify(curvePointService, times(0)).updateCurvePoint(any(CurvePoint.class));
    }

    @Test
    public void deleteCurvePointTest() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}
