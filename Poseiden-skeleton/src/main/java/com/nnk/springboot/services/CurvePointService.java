package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    public List<CurvePoint> getAll() {
        return curvePointRepository.findAll();
    }

    public CurvePoint getById(Integer id) {
        return curvePointRepository.findById(id).get();
    }

    public void createCurvePoint(CurvePoint curvePoint) {
        curvePointRepository.save(curvePoint);
    }

    public void updateCurvePoint(Integer id, CurvePoint curvePoint) {
        CurvePoint fetchedCurvePoint = curvePointRepository.findById(id).get();
        fetchedCurvePoint.setCurveId(curvePoint.getCurveId());
        fetchedCurvePoint.setTerm(curvePoint.getTerm());
        fetchedCurvePoint.setValue(curvePoint.getValue());
        curvePointRepository.save(fetchedCurvePoint);
    }

    public void deleteCurvePointById(Integer id) {
        curvePointRepository.deleteById(id);
    }
}
