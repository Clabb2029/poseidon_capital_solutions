package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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

@SpringBootTest
public class CurvePointServiceTests {

    @InjectMocks
    private CurvePointService curvePointService;

    @Mock
    private CurvePointRepository curvePointRepository;

    private CurvePoint curvePoint1;
    private CurvePoint curvePoint2;

    @BeforeEach
    public void setUp() {
        curvePoint1 = new CurvePoint(10, 1, new Timestamp(new Date().getTime()), 3d, 4d, new Timestamp(new Date().getTime()));
        curvePoint2 = new CurvePoint(20, 2, new Timestamp(new Date().getTime()), 5d, 6d, new Timestamp(new Date().getTime()));
    }

    @Test
    public void getAllTest() {
        List<CurvePoint> curvePoints = Arrays.asList(curvePoint1, curvePoint2);
        when(curvePointRepository.findAll()).thenReturn(curvePoints);
        List<CurvePoint> result = curvePointService.getAll();
        verify(curvePointRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void getByIdTest() {
        when(curvePointRepository.findById(curvePoint1.getId())).thenReturn(Optional.ofNullable(curvePoint1));
        curvePointService.createCurvePoint(curvePoint1);
        CurvePoint result = curvePointService.getById(curvePoint1.getId());
        assertEquals(curvePoint1, result);
        assertFalse(ObjectUtils.isEmpty(result));
        assertEquals(curvePoint1.getCurveId(), result.getCurveId());
        assertEquals(curvePoint1.getTerm(), result.getTerm());
        assertEquals(curvePoint1.getValue(), result.getValue());
    }

    @Test
    public void createCurvePointTest() {
        when(curvePointRepository.save(curvePoint1)).thenReturn(curvePoint1);
        curvePointService.createCurvePoint(curvePoint1);
        verify(curvePointRepository, times(1)).save(curvePoint1);
    }

    @Test
    public void updateCurvePointTest() {
        when(curvePointRepository.save(curvePoint1)).thenReturn(curvePoint1);
        curvePointService.updateCurvePoint(curvePoint1);
        verify(curvePointRepository, times(1)).save(curvePoint1);
    }

    @Test
    public void deleteCurvePointByIdTest() {
        when(curvePointRepository.findById(curvePoint1.getId())).thenReturn(Optional.ofNullable(curvePoint1));
        curvePointService.deleteCurvePointById(curvePoint1.getId());
        verify(curvePointRepository, times(1)).deleteById(curvePoint1.getId());
    }
}
