package com.example.demo.controller;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling notification-related requests.
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Display all notifications for the current user.
     *
     * @param user the authenticated user
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping
    public String getNotifications(@AuthenticationPrincipal User user, Model model) {
        List<Notification> notifications = notificationService.getNotificationsForUser(user);
        model.addAttribute("notifications", notifications);
        return "notification/list";
    }

    /**
     * Get the count of unread notifications for the current user.
     *
     * @param user the authenticated user
     * @return the count of unread notifications
     */
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal User user) {
        long count = notificationService.countUnreadNotificationsForUser(user);
        return ResponseEntity.ok(count);
    }

    /**
     * Mark a notification as read.
     *
     * @param id the ID of the notification to mark as read
     * @param user the authenticated user
     * @return redirect to the notifications page
     */
    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Optional<Notification> notification = notificationService.markNotificationAsRead(id);
        
        // If the notification has a related URL, redirect to it
        if (notification.isPresent() && notification.get().getRelatedUrl() != null) {
            return "redirect:" + notification.get().getRelatedUrl();
        }
        
        return "redirect:/notifications";
    }

    /**
     * Mark all notifications as read for the current user.
     *
     * @param user the authenticated user
     * @return redirect to the notifications page
     */
    @PostMapping("/read-all")
    public String markAllAsRead(@AuthenticationPrincipal User user) {
        notificationService.markAllNotificationsAsRead(user);
        return "redirect:/notifications";
    }

    /**
     * Delete a notification.
     *
     * @param id the ID of the notification to delete
     * @return redirect to the notifications page
     */
    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}