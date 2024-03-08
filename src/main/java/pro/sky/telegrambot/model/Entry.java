package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Entry {
    @Id
    private long id;
    private long chatId;
    private String notificationText;
    private LocalDateTime dateTime;

    public Entry() {}

    public Entry(long chatId, String notificationText, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.notificationText = notificationText;
        this.dateTime = dateTime;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return chatId == entry.chatId && Objects.equals(notificationText, entry.notificationText) && Objects.equals(dateTime, entry.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, notificationText, dateTime);
    }
}