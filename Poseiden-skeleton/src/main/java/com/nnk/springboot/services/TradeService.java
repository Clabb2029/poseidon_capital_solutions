package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> getAll() {
        return tradeRepository.findAll();
    }

    public Trade getById(Integer id) {
        return tradeRepository.findById(id).get();
    }

    public void createTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    public void updateTrade(Trade trade) {
        tradeRepository.save(trade);
    }

    public void deleteTradeById(Integer id) {
        tradeRepository.deleteById(id);
    }

}
