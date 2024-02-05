package com.projects.eventsbook.service;

public interface OrderService {
    void createOrder(Long userId, Long eventId, Long ticketTemplateId, Integer ticketsCount);
}
