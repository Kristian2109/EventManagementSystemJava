package com.projects.eventsbook.service.event;

import com.projects.eventsbook.DTO.eventDomain.CreateEventDTO;
import com.projects.eventsbook.DTO.eventDomain.CreateReviewDTO;
import com.projects.eventsbook.DTO.eventDomain.CreateTicketTemplateDTO;
import com.projects.eventsbook.DTO.eventDomain.EventDetails;
import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.Review;
import com.projects.eventsbook.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    public Event create(CreateEventDTO createEventDTO);
    // public void delete();
    public Event getById(Long eventId);
    public void addTicket(CreateTicketTemplateDTO createTicketTemplateDTO);
    // public void update();
    public List<Event> getBrowsingEventsForUser(User user, int pageNumber, int pageSize);
    public Review makeReviewToEvent(CreateReviewDTO createReviewDTO);
    // public void getReviewsOfEvent();
    public boolean canUserReviewEvent(Long userId, Long eventId);
    public EventDetails getEventDetailsForAdmin(Long eventId);
}
