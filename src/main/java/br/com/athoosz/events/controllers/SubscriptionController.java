package br.com.athoosz.events.controllers;

import br.com.athoosz.events.dto.ErrorMessage;
import br.com.athoosz.events.dto.SubscriptionResponse;
import br.com.athoosz.events.exceptions.EventNotFoundException;
import br.com.athoosz.events.exceptions.SubscriptionConflictException;
import br.com.athoosz.events.exceptions.UserIndicatorNotFoundException;
import br.com.athoosz.events.models.User;
import br.com.athoosz.events.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping({ "/subscription/{prettyName}", "subscription/{prettyName}/{userId}" })
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName,
                                                @RequestBody User subscriber,
            @PathVariable(required = false) Integer userId) {
        try {
            SubscriptionResponse result = subscriptionService.createNewSubscription(prettyName, subscriber, userId);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (EventNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        } catch (SubscriptionConflictException ex) {
            return ResponseEntity.status(409).body(new ErrorMessage(ex.getMessage()));
        } catch (UserIndicatorNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
        return ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/subscription/{prettyName}/ranking")
    public ResponseEntity<?> getRankingByEvent(@PathVariable String prettyName) {
        try {
            return ResponseEntity.ok(subscriptionService.getCompleteRanking(prettyName));
        } catch (EventNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
    }
}

