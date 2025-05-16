package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a notification for a user.
 * Used to notify users about new posts, comments, etc.
 */
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false)
    private String content;

    @Column(name = "related_url")
    private String relatedUrl;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public Notification() {
    }

    public Notification(User user, String type, String content, String relatedUrl) {
        this.user = user;
        this.type = type;
        this.content = content;
        this.relatedUrl = relatedUrl;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRelatedUrl() {
        return relatedUrl;
    }

    public void setRelatedUrl(String relatedUrl) {
        this.relatedUrl = relatedUrl;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", relatedUrl='" + relatedUrl + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}