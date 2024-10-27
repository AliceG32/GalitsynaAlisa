package org.lesson05;

public interface EnrichmentInterface {
  Message.EnrichmentType type();
  Message enrich(Message message);
}
