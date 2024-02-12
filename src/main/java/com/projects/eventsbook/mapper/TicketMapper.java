package com.projects.eventsbook.mapper;

import com.projects.eventsbook.DTO.eventDomain.EventDetails;
import com.projects.eventsbook.DTO.eventDomain.TicketTemplateDetails;
import com.projects.eventsbook.entity.Event;
import com.projects.eventsbook.entity.Review;
import com.projects.eventsbook.entity.TicketTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

public class TicketMapper {
    public static TicketTemplateDetails toTicketTemplateDetails(TicketTemplate ticketTemplate) {
        TicketTemplateDetails templateDetails = new TicketTemplateDetails();
        BeanUtils.copyProperties(ticketTemplate, templateDetails);
        Integer soldTicketsCount = ticketTemplate.getInitialTicketsCount() - ticketTemplate.getCurrentTicketsCount();
        templateDetails.setRevenue(ticketTemplate.getPrice() * soldTicketsCount);
        return templateDetails;
    }


}
