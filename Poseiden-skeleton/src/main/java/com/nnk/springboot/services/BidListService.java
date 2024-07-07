package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    public List<BidList> getAll() {
        return bidListRepository.findAll();
    }

    public BidList getById(Integer id) {
        return bidListRepository.findById(id).get();
    }

    public void createBidList(BidList bidList) {
        bidListRepository.save(bidList);
    }

    public void updateBidList(BidList bidList) {
        bidListRepository.save(bidList);
    }

    public void deleteBidListById(Integer id) {
        bidListRepository.deleteById(id);
    }

}
