package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    @Autowired
    private SettlementService settlementService;

    // Create a new settlement
    @PostMapping
    public ResponseEntity<Settlement> createSettlement(@RequestBody Settlement settlement) {
        Settlement created = settlementService.createSettlement(settlement);
        return ResponseEntity.ok(created);
    }

    // Get settlement by ID
    @GetMapping("/{id}")
    public ResponseEntity<Settlement> getSettlementById(@PathVariable Long id) {
        Optional<Settlement> settlement = settlementService.getSettlementById(id);
        return settlement.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all settlements
    @GetMapping
    public List<Settlement> getAllSettlements() {
        return settlementService.getAllSettlements();
    }

    // Delete a settlement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSettlement(@PathVariable Long id) {
        settlementService.deleteSettlement(id);
        return ResponseEntity.noContent().build();
    }
    // Update a settlement
    @PutMapping("/{id}")
    public ResponseEntity<Settlement> updateSettlement(@PathVariable Long id, @RequestBody Settlement settlement) {
        Settlement updated = settlementService.updateSettlement(id, settlement);
        return ResponseEntity.ok(updated);
    }

}
