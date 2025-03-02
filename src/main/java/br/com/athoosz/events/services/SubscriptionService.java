package br.com.athoosz.events.services;

import br.com.athoosz.events.dto.SubscriptionRankingByUser;
import br.com.athoosz.events.dto.SubscriptionRankingItem;
import br.com.athoosz.events.dto.SubscriptionResponse;
import br.com.athoosz.events.exceptions.EventNotFoundException;
import br.com.athoosz.events.exceptions.SubscriptionConflictException;
import br.com.athoosz.events.exceptions.UserIndicatorNotFoundException;
import br.com.athoosz.events.models.Event;
import br.com.athoosz.events.models.Subscription;
import br.com.athoosz.events.models.User;
import br.com.athoosz.events.repositories.EventRepository;
import br.com.athoosz.events.repositories.SubscriptionRepository;
import br.com.athoosz.events.repositories.UserRepository;

import java.util.List;
import java.util.stream.IntStream;

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

    public SubscriptionResponse createNewSubscription(String eventName, User user, Integer userId) {

        // recuperar o evento pelo nome
        Event event = eventRepository.findByPrettyName(eventName);
        if (event == null) { // Evento nao existe
            throw new EventNotFoundException("O Evento " + eventName + " Nao existe");
        }
        User userRecovered = userRepository.findByEmail(user.getEmail());
        if (userRecovered == null) { // Usuario existe na base, porem nao há inscricao dele
            userRecovered = userRepository.save(user);
        }
        User indicator = null;
        if (userId != null) {
            indicator = userRepository.findById(userId).orElse(null);
            if (indicator == null) {
                throw new UserIndicatorNotFoundException("Usuario " + userId + " indicador nao existe ");
            }
        }

        Subscription subscription = new Subscription();
        subscription.setEvent(event);
        subscription.setSubscriber(userRecovered);
        subscription.setIndication(indicator);

        Subscription tmpSub = subscriptionRepository.findByEventAndSubscriber(event, userRecovered);
        if (tmpSub != null) { // ja existe inscricao do usuario no evento
            throw new SubscriptionConflictException(
                    "Ja existe inscriçao para o usuario " + userRecovered.getName() + " no evento " + event.getTitle());
        }

        Subscription result = subscriptionRepository.save(subscription);
        return new SubscriptionResponse(result.getSubscriptionNumber(),
                "http://codecraft.com/subscription/" + result.getEvent().getPrettyName() + "/"
                        + result.getSubscriber().getUserId());
    }

    public List<SubscriptionRankingItem> getCompleteRanking(String prettyName) {
        Event event = eventRepository.findByPrettyName(prettyName);
        if (event == null) {
            throw new EventNotFoundException("Ranking do evento " + prettyName + " nao encontrado");
        }
        return subscriptionRepository.generateRanking(event.getEventId());
    }

    public SubscriptionRankingByUser getRankingByUser(String prettyName, Integer userId) {
        List<SubscriptionRankingItem> ranking = getCompleteRanking(prettyName);
        SubscriptionRankingItem item = ranking.stream().filter(i -> i.userId().equals(userId)).findFirst().orElse(null);
        if (item == null) {
            throw new UserIndicatorNotFoundException(
                    "Nao ha inscricao para o usuario " + userId + " no evento " + prettyName);
        }
        Integer position = IntStream.range(0, ranking.size()).filter(i -> ranking.get(i).userId().equals(userId))
                .findFirst().getAsInt();
        return new SubscriptionRankingByUser(item, position + 1);
    }
}
