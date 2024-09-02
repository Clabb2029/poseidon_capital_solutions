package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "Usertest", password = "passwordtest", roles = "USER")
public class BidListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    private BidList bidList1;
    private BidList bidList2;

    @BeforeEach
    public void setUp() {
        bidList1 = new BidList(1, "Account Test", "Type Test", 10d, 20d, 30d, 40d, "benchmark test", new Timestamp(new Date().getTime()), "commentary test", "secrity test", "status test", "trader test", "book test", "creationName test", new Timestamp(new Date().getTime()), "revisionName test", new Timestamp(new Date().getTime()), "dealName test", "dealType test", "sourceListId test", "side test");
        bidList2 = new BidList(2, "Account Test2", "Type Test2", 10d, 20d, 30d, 40d, "benchmark test2", new Timestamp(new Date().getTime()), "commentary test2", "secrity test2", "status test2", "trader test2", "book test2", "creationName test2", new Timestamp(new Date().getTime()), "revisionName test2", new Timestamp(new Date().getTime()), "dealName test2", "dealType test2", "sourceListId test2", "side test2");
    }

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attribute("username", "Usertest"))
                .andExpect(view().name("bidList/list"));
    }

    @Test
    public void addBidListTest() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    public void validateTest() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", bidList1.getAccount())
                        .param("type", bidList1.getType())
                        .param("bidQuantity", String.valueOf(bidList1.getBidQuantity()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
        verify(bidListService, times(1)).createBidList(any(BidList.class));
    }

    @Test
    public void validateWhenFormNotValidTest() throws Exception {
        mockMvc.perform(post("/bidList/validate"))
                .andExpect(view().name("bidList/add"));
        verify(bidListService, times(0)).createBidList(any(BidList.class));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        when(bidListService.getById(1)).thenReturn(bidList1);
        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("bidList/update"));
        verify(bidListService, times(1)).getById(1);
    }

    @Test
    public void showUpdateFormWhenNotFoundTest() throws Exception {
        when(bidListService.getById(1)).thenThrow(new IllegalArgumentException("No bidlist found."));
        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("bidList/update"));
        verify(bidListService, times(1)).getById(1);
    }

    @Test
    public void updateBidListTest() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .param("account", bidList2.getAccount())
                        .param("type", bidList2.getType())
                        .param("bidQuantity", String.valueOf(bidList2.getBidQuantity()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    public void updateBidListWhenFormNotValidTest() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));
        verify(bidListService, times(0)).updateBidList(any(BidList.class));
    }

    @Test
    public void deleteBidListTest() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }
}

