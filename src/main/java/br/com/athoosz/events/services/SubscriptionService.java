package br.com.athoosz.events.services;

import br.com.athoosz.events.exceptions.EventNotFoundException;
import br.com.athoosz.events.models.Event;
import br.com.athoosz.events.models.Subscription;
import br.com.athoosz.events.models.User;
import br.com.athoosz.events.repositories.EventRepository;
import br.com.athoosz.events.repositories.SubscriptionRepository;
import br.com.athoosz.events.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public Subscription createNewSubscription(String eventName, User user) {
        Subscription subscription = new Subscription();

        // recuperar o evento pelo nome
        Event event = eventRepository.findByPrettyName(eventName);
        if(event != null) {
            throw new EventNotFoundException("O Evento " + eventName + "Nao existe");
        }
        User userRecovered = userRepository.findByEmail(user.getEmail());
        if (userRecovered == null) {
            userRecovered = userRepository.save(user);
        }

        subscription.setEvent(event);
        subscription.setSubscriber(userRecovered);

        Subscription result = subscriptionRepository.save(subscription);
        return result;
    }
}
