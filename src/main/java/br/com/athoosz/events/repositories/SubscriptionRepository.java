package br.com.athoosz.events.repositories;

import br.com.athoosz.events.models.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

}
