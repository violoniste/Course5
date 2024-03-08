package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Entry;
import pro.sky.telegrambot.repository.EntryRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EntryService {
    private final EntryRepository repository;

    public EntryService(EntryRepository repository) {
        this.repository = repository;
    }

    public void schedule(Long chatId, String notificationText, LocalDateTime localDateTime) {
        Entry entry = new Entry(chatId, notificationText, localDateTime);
        repository.save(entry);
    }

    public List<Entry> getList() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return repository.findByDateTime(now);
    }
}
