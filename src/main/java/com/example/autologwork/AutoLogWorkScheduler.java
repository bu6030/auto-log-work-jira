package com.example.autologwork;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoLogWorkScheduler {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${jira.base.url}")
    private String jiraBaseUrl;

    @Value("${jira.log.work.url}")
    private String logWrokUrl;

    @Value("${jira.log.work.user.story.id}")
    private String userStoryId;

    @Value("${jira.log.work.username}")
    private String username;

    @Value("${jira.log.work.password}")
    private String password;

    @Value("${jira.log.work.content}")
    private String content;

    @Value("${jira.log.work.not-work-day}")
    private String notWorkDay;

    @Scheduled(cron = "${jira.log.work.cron}")
    public void autoLogWork() {
        log.info("======= AutoLogWorkScheduler started =======");
        execute();
        log.info("======= AutoLogWorkScheduler finished =======");
    }

    private void execute() {
        if (notWorkDay.contains(LocalDate.now().toString())) {
            log.info("Today isn't workday!");
            return;
        }
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    jiraBaseUrl + logWrokUrl,
                    HttpMethod.POST,
                    requestEntity(username, password, content),
                    String.class,
                    userStoryId);
            log.info("Log work response: {}", response);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.info("Send Packet throwable error", throwable);
        }

    }

    private HttpEntity<?> requestEntity(String username, String password, String content) {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth(username, password);
        Map<String, Object> params = new HashMap<>();
        params.put("timeSpent", "8h");
        params.put("started", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS")) + "+0800");
        params.put("comment", content);
        log.info("{}", params);
        return new HttpEntity<>(params, header);
    }
}
