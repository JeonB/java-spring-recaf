package com.example.demo.repository;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Notification entities.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find all notifications for a specific user, ordered by creation date (newest first).
     *
     * @param user the user to find notifications for
     * @return list of notifications for the user
     */
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    
    /**
     * Find all unread notifications for a specific user.
     *
     * @param user the user to find notifications for
     * @param isRead whether the notification has been read
     * @return list of unread notifications for the user
     */
    List<Notification> findByUserAndIsRead(User user, boolean isRead);
    
    /**
     * Count the number of unread notifications for a specific user.
     *
     * @param user the user to count notifications for
     * @param isRead whether the notification has been read
     * @return count of unread notifications
     */
    long countByUserAndIsRead(User user, boolean isRead);
}