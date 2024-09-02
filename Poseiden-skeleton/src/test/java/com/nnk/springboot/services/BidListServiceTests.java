package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BidListServiceTests {

    @InjectMocks
    private BidListService bidListService;

    @Mock
    private BidListRepository bidListRepository;

    private BidList bidList1;
    private BidList bidList2;


    @BeforeEach
    public void setUp() {
        bidList1 = new BidList(1, "Account Test", "Type Test", 10d, 20d, 30d, 40d, "benchmark test", new Timestamp(new Date().getTime()), "commentary test", "secrity test", "status test", "trader test", "book test", "creationName test", new Timestamp(new Date().getTime()), "revisionName test", new Timestamp(new Date().getTime()), "dealName test", "dealType test", "sourceListId test", "side test");
        bidList2 = new BidList(2, "Account Test2", "Type Test2", 10d, 20d, 30d, 40d, "benchmark test2", new Timestamp(new Date().getTime()), "commentary test2", "secrity test2", "status test2", "trader test2", "book test2", "creationName test2", new Timestamp(new Date().getTime()), "revisionName test2", new Timestamp(new Date().getTime()), "dealName test2", "dealType test2", "sourceListId test2", "side test2");
    }

    @Test
    public void getAllTest() {
        List<BidList> bidLists = Arrays.asList(bidList1, bidList2);
        when(bidListRepository.findAll()).thenReturn(bidLists);
        List<BidList> result = bidListService.getAll();
        verify(bidListRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void getByIdTest() {
        when(bidListRepository.findById(bidList1.getBidListId())).thenReturn(Optional.ofNullable(bidList1));
        bidListService.createBidList(bidList1);
        BidList result = bidListService.getById(bidList1.getBidListId());
        assertEquals(bidList1, result);
        assertFalse(ObjectUtils.isEmpty(result));
        assertEquals(bidList1.getAccount(), result.getAccount());
        assertEquals(bidList1.getType(), result.getType());
        assertEquals(bidList1.getBidQuantity(), result.getBidQuantity());
    }

    @Test
    public void createBidListTest() {
        when(bidListRepository.save(bidList1)).thenReturn(bidList1);
        bidListService.createBidList(bidList1);
        verify(bidListRepository, times(1)).save(bidList1);
    }

    @Test
    public void updateBidListTest() {
        when(bidListRepository.save(bidList1)).thenReturn(bidList1);
        bidListService.updateBidList(bidList1);
        verify(bidListRepository, times(1)).save(bidList1);
    }

    @Test
    public void deleteBidListByIdTest() {
        when(bidListRepository.findById(bidList1.getBidListId())).thenReturn(Optional.ofNullable(bidList1));
        bidListService.deleteBidListById(bidList1.getBidListId());
        verify(bidListRepository, times(1)).deleteById(bidList1.getBidListId());
    }
}

