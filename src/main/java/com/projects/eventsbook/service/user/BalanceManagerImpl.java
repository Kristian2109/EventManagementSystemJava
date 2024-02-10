package com.projects.eventsbook.service.user;

import com.projects.eventsbook.DAO.UserRepository;
import com.projects.eventsbook.entity.User;
import com.projects.eventsbook.exceptions.InvalidOperationException;
import com.projects.eventsbook.util.RetrieveUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BalanceManagerImpl implements BalanceManager {

    private final UserRepository userRepository;

    public BalanceManagerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void addMoneyToUser(Long userId, Double currentBalance, Double amountToAdd) {
        User currentUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        if (currentUser.getBalance() != currentBalance) {
            return;
        }
        currentUser.setBalance(currentBalance + amountToAdd);

        userRepository.save(currentUser);
    }

    @Override
    public void drawMoneyFromUser(Long userId, Double currentBalance, Double amountToDraw) {
        User currentUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        if (currentUser.getBalance() != currentBalance) {
            return;
        }
        if (currentUser.getBalance() < amountToDraw) {
            throw new InvalidOperationException("You have only " + currentBalance + ". Cannot draw " + amountToDraw + "!");
        }
        currentUser.setBalance(currentBalance - amountToDraw);
        userRepository.save(currentUser);
    }

    private void executeTransaction(Long userId, Double currentBalance, Double amount, String operation) {
        User currentUser = RetrieveUtil.getByIdWithException(this.userRepository, userId);
        if (currentUser.getBalance() != currentBalance) {
            return;
        }
        if (operation.equals("+")) {
            currentUser.setBalance(currentBalance + amount);
        } else if (operation.equals("-")) {
            currentUser.setBalance(currentBalance - amount);
        }

        userRepository.save(currentUser);
    }
}
