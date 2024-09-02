package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class TradeServiceTests {
    
    @InjectMocks
    private TradeService tradeService;
    
    @Mock
    private TradeRepository tradeRepository;
    
    private Trade trade1;
    private Trade trade2;
    
    @BeforeEach
    public void setUp() {
        trade1 = new Trade(1, "Trade Account", "Type", 10d, 20d, 30d, 40d, "benchmark", new Timestamp(new Date().getTime()), "security", "status", "trader", "book", "creationName", new Timestamp(new Date().getTime()), "revisionName", new Timestamp(new Date().getTime()), "dealName", "dealType", "sourceListId", "side");
        trade2 = new Trade(2, "Trade Account2", "Type2", 50d, 60d, 70d, 90d, "benchmark2", new Timestamp(new Date().getTime()), "security2", "status2", "trader2", "book2", "creationName2", new Timestamp(new Date().getTime()), "revisionName2", new Timestamp(new Date().getTime()), "dealName2", "dealType2", "sourceListId2", "side2");
    }

    @Test
    public void getAllTest() {
        List<Trade> trades = Arrays.asList(trade1, trade2);
        when(tradeRepository.findAll()).thenReturn(trades);
        List<Trade> result = tradeService.getAll();
        verify(tradeRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void getByIdTest() {
        when(tradeRepository.findById(trade1.getTradeId())).thenReturn(Optional.ofNullable(trade1));
        tradeService.createTrade(trade1);
        Trade result = tradeService.getById(trade1.getTradeId());
        assertEquals(trade1, result);
        assertFalse(ObjectUtils.isEmpty(result));
        assertEquals(trade1.getAccount(), result.getAccount());
        assertEquals(trade1.getType(), result.getType());
        assertEquals(trade1.getBuyQuantity(), result.getBuyQuantity());
    }

    @Test
    public void createTradeTest() {
        when(tradeRepository.save(trade1)).thenReturn(trade1);
        tradeService.createTrade(trade1);
        verify(tradeRepository, times(1)).save(trade1);
    }

    @Test
    public void updateTradeTest() {
        when(tradeRepository.save(trade1)).thenReturn(trade1);
        tradeService.updateTrade(trade1);
        verify(tradeRepository, times(1)).save(trade1);
    }

    @Test
    public void deleteTradeByIdTest() {
        when(tradeRepository.findById(trade1.getTradeId())).thenReturn(Optional.ofNullable(trade1));
        tradeService.deleteTradeById(trade1.getTradeId());
        verify(tradeRepository, times(1)).deleteById(trade1.getTradeId());
    }
}
