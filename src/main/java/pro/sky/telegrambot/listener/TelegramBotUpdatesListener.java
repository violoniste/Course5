package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Entry;
import pro.sky.telegrambot.service.EntryService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    Pattern pattern = Pattern.compile("([0-9.:\\s]{16})(\\s)([\\W+]+)");

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private EntryService service;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach( update -> {
//            logger.info("Processing update: {}", update);

            Message message = update.message();
            Long chatId = message.chat().id();
            String text = message.text();

            logger.info("Message: {}", text);

            if (text.equals("/start")) {
                onStartReceived(chatId);
            }

            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                onScheduleMessage(chatId, matcher.group(1), matcher.group(3));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void onStartReceived(Long chatId) {
        logger.info("Start received!");
        sendMessage(chatId, "Вас приветствует корпорация \"Irongate Apps\"");
    }

    private void onScheduleMessage(Long chatId, String dateTimeStr, String text) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        service.schedule(chatId, text, localDateTime);

        sendMessage(chatId, "\"" + text + "\" запланировано!");
        logger.info("Scheduled: {}", text);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void checkList() {
        List<Entry> list = service.getList();
        for (Entry b : list) {
            sendMessage(b.getChatId(), b.getNotificationText());
            logger.info("Sent: {}", b.getNotificationText());
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        SendResponse response = telegramBot.execute(message);

        if (!response.isOk()) {
            logger.error("Send message error: {}", response.errorCode());
        }
    }
}
