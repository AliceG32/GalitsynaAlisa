package org.lesson05;

import java.util.List;

public class EnrichmentService {

  private final List<EnrichmentInterface> enrichments;
  public EnrichmentService(List<EnrichmentInterface> enrichments) {
    this.enrichments = enrichments;
  }

  public synchronized Message enrich(Message message) {
    if (message == null) {
      throw new IllegalArgumentException();
    }

    for (EnrichmentInterface enrichment : this.enrichments) {
      if (enrichment.type().equals(message.enrichmentType)) {
        return enrichment.enrich(message);
      }
    }

    return message;
  }
}
