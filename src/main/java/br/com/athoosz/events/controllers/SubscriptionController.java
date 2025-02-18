package br.com.athoosz.events.controllers;

import br.com.athoosz.events.dto.ErrorMessage;
import br.com.athoosz.events.exceptions.EventNotFoundException;
import br.com.athoosz.events.models.Subscription;
import br.com.athoosz.events.models.User;
import br.com.athoosz.events.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscription/{prettyName}")
    public ResponseEntity<?> createSubscription(@PathVariable String prettyName, @RequestBody User subscriber) {
        try {
            Subscription result = subscriptionService.createNewSubscription(prettyName, subscriber);
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (EventNotFoundException ex) {
            return ResponseEntity.status(404).body(new ErrorMessage(ex.getMessage()));
        }
     return ResponseEntity.badRequest().build();
    }
}

