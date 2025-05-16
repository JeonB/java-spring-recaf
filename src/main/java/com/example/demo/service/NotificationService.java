package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing notifications.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Create a new notification.
     *
     * @param user the user to notify
     * @param type the type of notification
     * @param content the content of the notification
     * @param relatedUrl the URL related to the notification
     * @return the created notification
     */
    @Transactional
    public Notification createNotification(User user, String type, String content, String relatedUrl) {
        Notification notification = new Notification(user, type, content, relatedUrl);
        return notificationRepository.save(notification);
    }

    /**
     * Get all notifications for a user.
     *
     * @param user the user to get notifications for
     * @return list of notifications for the user
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    /**
     * Get unread notifications for a user.
     *
     * @param user the user to get notifications for
     * @return list of unread notifications for the user
     */
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserAndIsRead(user, false);
    }

    /**
     * Count unread notifications for a user.
     *
     * @param user the user to count notifications for
     * @return count of unread notifications
     */
    @Transactional(readOnly = true)
    public long countUnreadNotificationsForUser(User user) {
        return notificationRepository.countByUserAndIsRead(user, false);
    }

    /**
     * Mark a notification as read.
     *
     * @param notificationId the ID of the notification to mark as read
     * @return the updated notification, or empty if not found
     */
    @Transactional
    public Optional<Notification> markNotificationAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setRead(true);
            return Optional.of(notificationRepository.save(notification));
        }
        return Optional.empty();
    }

    /**
     * Mark all notifications for a user as read.
     *
     * @param user the user whose notifications to mark as read
     */
    @Transactional
    public void markAllNotificationsAsRead(User user) {
        List<Notification> unreadNotifications = notificationRepository.findByUserAndIsRead(user, false);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    /**
     * Delete a notification.
     *
     * @param notificationId the ID of the notification to delete
     */
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}