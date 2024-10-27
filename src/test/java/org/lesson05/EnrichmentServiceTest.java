package org.lesson05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnrichmentServiceTest {
  private EnrichmentService enrichmentService;

  @BeforeEach
  public void setUp() {
    UserRepositoryInterface userRepository = new InMemoryUserRepository();
    userRepository.updateUserByMsisdn("88005553535", new User("Egor", "Petrov"));
    this.enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn(userRepository)));
  }

  @Test
  public void testEnrichment() {
    Map<String, String> input = new HashMap<>();
    input.put("action", "button_click");
    input.put("page", "book_card");
    input.put("msisdn", "88005553535");

    Message message = new Message(input, Message.EnrichmentType.MSISDN);
    Message resultMessage = this.enrichmentService.enrich(message);

    Map<String, String> enrichedContent = resultMessage.getContent();
    assertEquals("Egor", enrichedContent.get("firstName"));
    assertEquals("Petrov", enrichedContent.get("lastName"));
  }
}