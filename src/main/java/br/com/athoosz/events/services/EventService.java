package br.com.athoosz.events.services;

import br.com.athoosz.events.models.Event;
import br.com.athoosz.events.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addNewEvent(Event event){
        // gerando o prettyname
        event.setPrettyName(event.getTitle().toUpperCase().replaceAll(" ", "_"));
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }

    public Event getEventByPrettyName(String prettyName){
        return eventRepository.findByPrettyName(prettyName);
    }
}
