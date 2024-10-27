package org.lesson05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnrichmentServiceConcurrentTest {
    private EnrichmentService enrichmentService;

  @BeforeEach
    public void setUp() {
    UserRepositoryInterface userRepository = new InMemoryUserRepository();
      userRepository.updateUserByMsisdn("88005553535", new User("Egor", "Petrov"));
      this.enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn(userRepository)));
    }

    @Test
    public void testConcurrencyEnrichment() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        List<Message> enrichmentResults = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                Map<String, String> input = new HashMap<>();
                input.put("action", "button_click");
                input.put("page", "book_card");
                input.put("msisdn", "88005553535");

                Message message = new Message(input, Message.EnrichmentType.MSISDN);
                Message enrichedMessage = enrichmentService.enrich(message);
                enrichmentResults.add(enrichedMessage);
                latch.countDown();
            });
        }

        latch.await();
        executorService.shutdown();

        for (Message enrichedMessage : enrichmentResults) {
            Map<String, String> enrichedContent = enrichedMessage.getContent();
            assertEquals("Egor", enrichedContent.get("firstName"));
            assertEquals("Petrov", enrichedContent.get("lastName"));
        }
    }
}
