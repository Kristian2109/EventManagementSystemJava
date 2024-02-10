package com.projects.eventsbook.service.user;

public interface BalanceManager {
    void addMoneyToUser(Long userId, Double currentAmount, Double amountToAdd);
    void drawMoneyFromUser(Long userId, Double currentAmount, Double amountToDraw);
}
