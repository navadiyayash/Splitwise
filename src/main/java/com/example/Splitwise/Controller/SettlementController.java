package com.example.Splitwise.Controller;

import com.example.Splitwise.Entity.Settlement;
import com.example.Splitwise.Service.SettlementService;
import com.example.Splitwise.Exception.SettlementNotFoundException;
import com.example.Splitwise.Exception.UserNotFoundException;
import com.example.Splitwise.Exception.GroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<?> createSettlement(@RequestBody Settlement settlement) {
        try {
            Settlement created = settlementService.createSettlement(settlement);
            return ResponseEntity.ok(created);
        } catch (UserNotFoundException | GroupNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Invalid settlement data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // Get settlement by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getSettlementById(@PathVariable Long id) {
        try {
            Optional<Settlement> settlement = settlementService.getSettlementById(id);
            return settlement.map(ResponseEntity::ok)
                    .orElseThrow(() -> new SettlementNotFoundException("Settlement not found with id: " + id));
        } catch (SettlementNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // Get all settlements
    @GetMapping
    public ResponseEntity<?> getAllSettlements() {
        try {
            return ResponseEntity.ok(settlementService.getAllSettlements());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // Delete a settlement
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSettlement(@PathVariable Long id) {
        try {
            settlementService.deleteSettlement(id);
            return ResponseEntity.noContent().build();
        } catch (SettlementNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

    // Update a settlement
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSettlement(@PathVariable Long id, @RequestBody Settlement settlement) {
        try {
            Settlement updated = settlementService.updateSettlement(id, settlement);
            return ResponseEntity.ok(updated);
        } catch (SettlementNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (UserNotFoundException | GroupNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Invalid settlement data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error: " + e.getMessage());
        }
    }

}
