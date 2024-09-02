package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "Usertest", password = "passwordtest", roles = "USER")
public class TradeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    private Trade trade1;
    private Trade trade2;

    @BeforeEach
    public void setUp() {
        trade1 = new Trade(1, "Trade Account", "Type", 10d, 20d, 30d, 40d, "benchmark", new Timestamp(new Date().getTime()), "security", "status", "trader", "book", "creationName", new Timestamp(new Date().getTime()), "revisionName", new Timestamp(new Date().getTime()), "dealName", "dealType", "sourceListId", "side");
        trade2 = new Trade(2, "Trade Account2", "Type2", 50d, 60d, 70d, 90d, "benchmark2", new Timestamp(new Date().getTime()), "security2", "status2", "trader2", "book2", "creationName2", new Timestamp(new Date().getTime()), "revisionName2", new Timestamp(new Date().getTime()), "dealName2", "dealType2", "sourceListId2", "side2");
    }

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attribute("username", "Usertest"))
                .andExpect(view().name("trade/list"));
    }

    @Test
    public void addTradeTest() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    public void validateTest() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", trade1.getAccount())
                        .param("type", trade1.getType())
                        .param("buyQuantity", String.valueOf(trade1.getBuyQuantity()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
        verify(tradeService, times(1)).createTrade(any(Trade.class));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        when(tradeService.getById(1)).thenReturn(trade1);
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("trade/update"));
        verify(tradeService, times(1)).getById(1);
    }

    @Test
    public void showUpdateFormWhenNotFoundTest() throws Exception {
        when(tradeService.getById(1)).thenThrow(new IllegalArgumentException("No trade found."));
        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("trade/update"));
        verify(tradeService, times(1)).getById(1);
    }

    @Test
    public void updateTradeTest() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .param("account", trade2.getAccount())
                        .param("type", trade2.getType())
                        .param("buyQuantity", String.valueOf(trade2.getBuyQuantity()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    public void deleteTradeTest() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }
}
