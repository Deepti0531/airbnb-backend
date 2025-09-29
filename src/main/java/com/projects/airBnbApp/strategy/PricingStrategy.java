package com.projects.airBnbApp.strategy;

import com.projects.airBnbApp.entity.Inventory;
import com.projects.airBnbApp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


public interface PricingStrategy {
    public BigDecimal calculatePrice(Inventory inventory);
}
