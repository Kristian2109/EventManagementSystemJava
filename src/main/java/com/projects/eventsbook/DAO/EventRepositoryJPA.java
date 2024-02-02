package com.projects.eventsbook.DAO;

import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.TicketTemplate;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepositoryJPA extends JpaRepository<Event, Long> {
    @Query(value =
            "SELECT \n" +
            "    e \n" +
            "FROM\n" +
            "    User u\n" +
            "        JOIN\n" +
            "    GroupMember gm ON u = gm.user\n" +
            "        JOIN\n" +
            "    Event e ON e.eventGroup = gm.eventGroup \n" +
            "   WHERE u.id = :userId \n" +
            "UNION SELECT \n" +
            "    e\n" +
            "FROM\n" +
            "    EventGroup gr\n" +
            "\t\tJOIN\n" +
            "    Event e ON gr = e.eventGroup\n" +
            "WHERE\n" +
            "    gr.isPrivate = FALSE\n")
    List<Event> getUserAccessibleEvents(Long userId);
    @Query("SELECT Event from Event where isFree = true")
    public List<Event> getAllPrivateEvents();

    public List<Event> findByEventGroup_IsPrivateIs(boolean isPrivate);
    @Query(value = "select count(r)" +
            " from Review r where r.user.id =: userId  and r.event.id =: eventId")
    int userReviewsCount(Long userId, Long eventId);

    @Override
    @EntityGraph("Event.relations")
    @NonNull
    Optional<Event> findById(@NonNull Long id);

    @Query("select ticket from TicketTemplate ticket where ticket.id = ?1")
    Optional<TicketTemplate> findTicketTemplateById(Long id);
}
