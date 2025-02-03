//package com.rts.tap.serviceimplementation;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.rts.tap.dao.MRFCandidateDao;
//import com.rts.tap.model.Candidate;
//import com.rts.tap.model.MRFCandidate;
//
//class MRFCandidateServiceImplTest {
//
//    @InjectMocks
//    private MRFCandidateServiceImpl mrfCandidateService;
//
//    @Mock
//    private MRFCandidateDao mrfCandidateRepository;
//
//    private MRFCandidate mrfCandidate1;
//    private MRFCandidate mrfCandidate2;
//    private Candidate candidate1;
//    private Candidate candidate2;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        candidate1 = new Candidate(); 
//        candidate1.setCandidateId(1L);
//        candidate2 = new Candidate(); 
//        candidate2.setCandidateId(2L); 
//
//        mrfCandidate1 = new MRFCandidate();
//        mrfCandidate1.setMrfCandidateId(1L);
//        mrfCandidate1.setCandidate(Arrays.asList(candidate1));
//        mrfCandidate1.setStatus("Applied");
//
//        mrfCandidate2 = new MRFCandidate();
//        mrfCandidate2.setMrfCandidateId(2L);
//        mrfCandidate2.setCandidate(Arrays.asList(candidate2));
//        mrfCandidate2.setStatus("Interview Scheduled");
//    }
//
//    @Test
//    void testGetAllCandidates() {
//        when(mrfCandidateRepository.getAllCandidates()).thenReturn(Arrays.asList(mrfCandidate1, mrfCandidate2));
//
//        List<MRFCandidate> result = mrfCandidateService.getAllCandidates();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(mrfCandidateRepository, times(1)).getAllCandidates();
//    }
//
//    @Test
//    void testGetAllCandidates_Empty() {
//        when(mrfCandidateRepository.getAllCandidates()).thenReturn(Collections.emptyList());
//
//        List<MRFCandidate> result = mrfCandidateService.getAllCandidates();
//
//        assertTrue(result.isEmpty());
//        verify(mrfCandidateRepository, times(1)).getAllCandidates();
//    }
//
//    @Test
//    void testGetCandidateById_Success() {
//        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(mrfCandidate1);
//
//        MRFCandidate result = mrfCandidateService.getCandidateById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getMrfCandidateId());
//        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
//    }
//
//    @Test
//    void testGetCandidateById_NotFound() {
//        when(mrfCandidateRepository.getCandidateById(3L)).thenReturn(null);
//
//        MRFCandidate result = mrfCandidateService.getCandidateById(3L);
//
//        assertNull(result);
//        verify(mrfCandidateRepository, times(1)).getCandidateById(3L);
//    }
//
//    @Test
//    void testSaveCandidate() {
//        when(mrfCandidateRepository.saveCandidate(any(MRFCandidate.class))).thenReturn(mrfCandidate1);
//
//        MRFCandidate result = mrfCandidateService.saveCandidate(mrfCandidate1);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getMrfCandidateId());
//        verify(mrfCandidateRepository, times(1)).saveCandidate(any(MRFCandidate.class));
//    }
//
//    @Test
//    void testUpdateCandidate_Success() {
//        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(mrfCandidate1);
//        when(mrfCandidateRepository.saveCandidate(any(MRFCandidate.class))).thenReturn(mrfCandidate1);
//
//        MRFCandidate updatedCandidate = new MRFCandidate();
//        updatedCandidate.setCandidate(Arrays.asList(candidate2));
//        updatedCandidate.setStatus("Updated Status");
//
//        MRFCandidate result = mrfCandidateService.updateCandidate(1L, updatedCandidate);
//
//        assertNotNull(result);
//        assertNotEquals(mrfCandidate1.getCandidate(), result.getCandidate());
//        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
//        verify(mrfCandidateRepository, times(1)).saveCandidate(any(MRFCandidate.class));
//    }
//
//    @Test
//    void testUpdateCandidate_NotFound() {
//        when(mrfCandidateRepository.getCandidateById(3L)).thenReturn(null);
//
//        MRFCandidate result = mrfCandidateService.updateCandidate(3L, mrfCandidate1);
//
//        assertNull(result);
//        verify(mrfCandidateRepository, times(1)).getCandidateById(3L);
//    }
//
//    @Test
//    void testDeleteCandidate() {
//        doNothing().when(mrfCandidateRepository).deleteCandidate(1L);
//
//        assertDoesNotThrow(() -> mrfCandidateService.deleteCandidate(1L));
//
//        verify(mrfCandidateRepository, times(1)).deleteCandidate(1L);
//    }
//
//    @Test
//    void testGetCandidateByScoreId() {
//        when(mrfCandidateRepository.getRemainingCandidates(1L)).thenReturn(Arrays.asList(candidate1, candidate2));
//
//        List<Candidate> result = mrfCandidateService.getCandidateByScoreId(1L);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(mrfCandidateRepository, times(1)).getRemainingCandidates(1L);
//    }
//
//    @Test
//    void testGetYetToOfferCandidateByMrfId() {
//        when(mrfCandidateRepository.getYetToOfferCandidateByMrfId(1L)).thenReturn(Arrays.asList(candidate1, candidate2));
//
//        List<Candidate> result = mrfCandidateService.getYetToOfferCandidateByMrfId(1L);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(mrfCandidateRepository, times(1)).getYetToOfferCandidateByMrfId(1L);
//    }
//
//}


package com.rts.tap.serviceimplementation;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rts.tap.dao.MRFCandidateDao;
import com.rts.tap.exception.CandidateNotFoundException;
import com.rts.tap.exception.CandidateSaveException;
import com.rts.tap.model.Candidate;
import com.rts.tap.model.MRFCandidate;
import com.rts.tap.model.MRFRecruiters;
 
@ExtendWith(MockitoExtension.class)
public class MRFCandidateServiceImplTest {
 
    @Mock
    private MRFCandidateDao mrfCandidateRepository;
 
    @InjectMocks
    private MRFCandidateServiceImpl mrfCandidateService;
 
    private MRFCandidate mrfCandidate;
    private Candidate candidate;
    private MRFRecruiters mrfRecruiter;
 
    @BeforeEach
    void setUp() {
        mrfRecruiter = new MRFRecruiters();
        mrfRecruiter.setMrfRecruitersId(1L);
 
        candidate = new Candidate();
        candidate.setCandidateId(1L);
 
        mrfCandidate = new MRFCandidate();
        mrfCandidate.setMrfCandidateId(1L);
        mrfCandidate.setCandidate(Collections.singletonList(candidate));
        mrfCandidate.setMrfRecruiter(mrfRecruiter);
        mrfCandidate.setStatus("Pending");
    }
 
    @Test
    void testGetAllCandidates() {
        when(mrfCandidateRepository.getAllCandidates()).thenReturn(Collections.singletonList(mrfCandidate));
 
        List<MRFCandidate> result = mrfCandidateService.getAllCandidates();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mrfCandidateRepository, times(1)).getAllCandidates();
    }
 
    @Test
    void testGetAllCandidates_ThrowsException() {
        when(mrfCandidateRepository.getAllCandidates()).thenReturn(Collections.emptyList());
 
        assertThrows(CandidateNotFoundException.class, () -> mrfCandidateService.getAllCandidates());
        verify(mrfCandidateRepository, times(1)).getAllCandidates();
    }
 
    @Test
    void testGetCandidateById() {
        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(mrfCandidate);
 
        MRFCandidate result = mrfCandidateService.getCandidateById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getMrfCandidateId());
        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
    }
 
    @Test
    void testGetCandidateById_ThrowsException() {
        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(null);
 
        assertThrows(CandidateNotFoundException.class, () -> mrfCandidateService.getCandidateById(1L));
        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
    }
 
    @Test
    void testSaveCandidate() {
        when(mrfCandidateRepository.saveCandidate(mrfCandidate)).thenReturn(mrfCandidate);
 
        MRFCandidate result = mrfCandidateService.saveCandidate(mrfCandidate);
        assertNotNull(result);
        assertEquals(1L, result.getMrfCandidateId());
        verify(mrfCandidateRepository, times(1)).saveCandidate(mrfCandidate);
    }
 
    @Test
    void testSaveCandidate_ThrowsException() {
        when(mrfCandidateRepository.saveCandidate(mrfCandidate)).thenThrow(new RuntimeException("Database error"));
 
        assertThrows(CandidateSaveException.class, () -> mrfCandidateService.saveCandidate(mrfCandidate));
        verify(mrfCandidateRepository, times(1)).saveCandidate(mrfCandidate);
    }
 
    @Test
    void testUpdateCandidate() {
        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(mrfCandidate);
        when(mrfCandidateRepository.saveCandidate(mrfCandidate)).thenReturn(mrfCandidate);
 
        MRFCandidate updatedCandidate = new MRFCandidate();
        updatedCandidate.setStatus("Approved");
 
        MRFCandidate result = mrfCandidateService.updateCandidate(1L, updatedCandidate);
        assertNotNull(result);
        assertEquals("Approved", result.getStatus());
        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
        verify(mrfCandidateRepository, times(1)).saveCandidate(mrfCandidate);
    }
 
    @Test
    void testUpdateCandidate_ThrowsException() {
        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(null);
 
        assertThrows(CandidateNotFoundException.class, () -> mrfCandidateService.updateCandidate(1L, mrfCandidate));
        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
    }
 
    @Test
    void testDeleteCandidate() {
        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(mrfCandidate);
        doNothing().when(mrfCandidateRepository).deleteCandidate(1L);
 
        mrfCandidateService.deleteCandidate(1L);
        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
        verify(mrfCandidateRepository, times(1)).deleteCandidate(1L);
    }
 
    @Test
    void testDeleteCandidate_ThrowsException() {
        when(mrfCandidateRepository.getCandidateById(1L)).thenReturn(null);
 
        assertThrows(CandidateNotFoundException.class, () -> mrfCandidateService.deleteCandidate(1L));
        verify(mrfCandidateRepository, times(1)).getCandidateById(1L);
    }
 
    @Test
    void testGetCandidateByScoreId() {
        when(mrfCandidateRepository.getRemainingCandidates(1L)).thenReturn(Collections.singletonList(candidate));
 
        List<Candidate> result = mrfCandidateService.getCandidateByScoreId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mrfCandidateRepository, times(1)).getRemainingCandidates(1L);
    }
 
    @Test
    void testGetCandidateByScoreId_ThrowsException() {
        when(mrfCandidateRepository.getRemainingCandidates(1L)).thenReturn(Collections.emptyList());
 
        assertThrows(CandidateNotFoundException.class, () -> mrfCandidateService.getCandidateByScoreId(1L));
        verify(mrfCandidateRepository, times(1)).getRemainingCandidates(1L);
    }
 
    @Test
    void testGetYetToOfferCandidateByMrfId() {
        when(mrfCandidateRepository.getYetToOfferCandidateByMrfId(1L)).thenReturn(Collections.singletonList(candidate));
 
        List<Candidate> result = mrfCandidateService.getYetToOfferCandidateByMrfId(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mrfCandidateRepository, times(1)).getYetToOfferCandidateByMrfId(1L);
    }
 
    @Test
    void testGetYetToOfferCandidateByMrfId_ThrowsException() {
        when(mrfCandidateRepository.getYetToOfferCandidateByMrfId(1L)).thenReturn(Collections.emptyList());
 
        assertThrows(CandidateNotFoundException.class, () -> mrfCandidateService.getYetToOfferCandidateByMrfId(1L));
        verify(mrfCandidateRepository, times(1)).getYetToOfferCandidateByMrfId(1L);
    }
}
 