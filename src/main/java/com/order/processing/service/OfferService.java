package com.order.processing.service;

import com.order.processing.dto.OfferResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to provide latest offers and deals to customers
 * Enhancement: Give choices of latest offers during order processing
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    public List<OfferResponse> getLatestOffers() {
        log.info("Fetching latest offers for customers");
        
        List<OfferResponse> offers = new ArrayList<>();
        
        offers.add(OfferResponse.builder()
                .offerId("OFFER-001")
                .offerTitle("Spring Sale - 30% Off")
                .description("Get 30% off on all electronics. Limited time offer!")
                .discountPercentage(30)
                .minOrderAmount(new BigDecimal("100.00"))
                .maxDiscount(new BigDecimal("50.00"))
                .validUntil(LocalDateTime.now().plusDays(7))
                .category("Electronics")
                .active(true)
                .build());
        
        offers.add(OfferResponse.builder()
                .offerId("OFFER-002")
                .offerTitle("Buy 1 Get 1 Free")
                .description("Buy any product and get another one free! Same or lesser value.")
                .discountPercentage(50)
                .minOrderAmount(new BigDecimal("50.00"))
                .maxDiscount(new BigDecimal("100.00"))
                .validUntil(LocalDateTime.now().plusDays(5))
                .category("Fashion")
                .active(true)
                .build());
        
        offers.add(OfferResponse.builder()
                .offerId("OFFER-003")
                .offerTitle("Free Shipping")
                .description("Free shipping on orders above $75. No code needed!")
                .discountPercentage(0)
                .minOrderAmount(new BigDecimal("75.00"))
                .maxDiscount(new BigDecimal("15.00"))
                .validUntil(LocalDateTime.now().plusDays(30))
                .category("All")
                .active(true)
                .build());
        
        offers.add(OfferResponse.builder()
                .offerId("OFFER-004")
                .offerTitle("Flash Deal - 40% Off")
                .description("Flash sale! 40% off on selected items. Hurry, limited stock!")
                .discountPercentage(40)
                .minOrderAmount(new BigDecimal("150.00"))
                .maxDiscount(new BigDecimal("75.00"))
                .validUntil(LocalDateTime.now().plusHours(12))
                .category("Home & Kitchen")
                .active(true)
                .build());
        
        log.info("Retrieved {} active offers", offers.size());
        return offers;
    }

    public List<OfferResponse> getOffersForOrder(BigDecimal orderAmount, String category) {
        log.info("Fetching applicable offers for order amount: {} in category: {}", orderAmount, category);
        
        List<OfferResponse> allOffers = getLatestOffers();
        List<OfferResponse> applicableOffers = new ArrayList<>();
        
        for (OfferResponse offer : allOffers) {
            if (offer.isActive() && 
                orderAmount.compareTo(offer.getMinOrderAmount()) >= 0 &&
                (offer.getCategory().equals("All") || offer.getCategory().equals(category))) {
                applicableOffers.add(offer);
            }
        }
        
        log.info("Found {} applicable offers for this order", applicableOffers.size());
        return applicableOffers;
    }

    public OfferResponse getBestOffer(BigDecimal orderAmount, String category) {
        log.info("Finding best offer for order amount: {} in category: {}", orderAmount, category);
        
        List<OfferResponse> applicableOffers = getOffersForOrder(orderAmount, category);
        
        if (applicableOffers.isEmpty()) {
            log.info("No applicable offers found");
            return null;
        }
        
        // Return offer with highest discount percentage
        OfferResponse bestOffer = applicableOffers.stream()
                .max((o1, o2) -> o1.getDiscountPercentage().compareTo(o2.getDiscountPercentage()))
                .orElse(null);
        
        log.info("Best offer found: {} with {}% discount", 
                bestOffer != null ? bestOffer.getOfferTitle() : "None", 
                bestOffer != null ? bestOffer.getDiscountPercentage() : 0);
        
        return bestOffer;
    }
}

