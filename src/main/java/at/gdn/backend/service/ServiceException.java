package at.gdn.backend.service;

import at.gdn.backend.entities.User;
import jakarta.persistence.PersistenceException;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ServiceException whileCreatingOrder(User.UserId userId, PersistenceException pEx) {
        var message = String.format("Cannot create order with quantity %d with user ID %d", userId.id());
        return new ServiceException(message, pEx);
    }
}