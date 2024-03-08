package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Entry;

import java.time.LocalDateTime;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByDateTime(LocalDateTime dateTime);
}
