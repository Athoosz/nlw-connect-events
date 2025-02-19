package br.com.athoosz.events.repositories;

import br.com.athoosz.events.models.Event;
import br.com.athoosz.events.models.Subscription;
import br.com.athoosz.events.models.User;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    public Subscription findByEventAndSubscriber(Event event, User user);
}
