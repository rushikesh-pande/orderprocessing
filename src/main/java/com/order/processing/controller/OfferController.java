package com.order.processing.controller;

import com.order.processing.dto.OfferResponse;
import com.order.processing.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for offer management and recommendations
 * Enhancement: Provide latest offers to customers during order processing
 */
@RestController
@RequestMapping("/api/v1/offers")
@RequiredArgsConstructor
@Slf4j
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/latest")
    public ResponseEntity<List<OfferResponse>> getLatestOffers() {
        log.info("Received request for latest offers");
        
        List<OfferResponse> offers = offerService.getLatestOffers();
        
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/applicable")
    public ResponseEntity<List<OfferResponse>> getApplicableOffers(
            @RequestParam BigDecimal orderAmount,
            @RequestParam(defaultValue = "All") String category) {
        log.info("Received request for applicable offers - amount: {}, category: {}", orderAmount, category);
        
        List<OfferResponse> offers = offerService.getOffersForOrder(orderAmount, category);
        
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/best")
    public ResponseEntity<OfferResponse> getBestOffer(
            @RequestParam BigDecimal orderAmount,
            @RequestParam(defaultValue = "All") String category) {
        log.info("Received request for best offer - amount: {}, category: {}", orderAmount, category);
        
        OfferResponse bestOffer = offerService.getBestOffer(orderAmount, category);
        
        if (bestOffer == null) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(bestOffer);
    }
}

