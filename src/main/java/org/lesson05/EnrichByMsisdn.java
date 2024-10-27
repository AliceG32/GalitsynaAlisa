package org.lesson05;

public class EnrichByMsisdn implements EnrichmentInterface {
  private final UserRepositoryInterface userRepository;

  public EnrichByMsisdn(UserRepositoryInterface userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Message.EnrichmentType type() {
    return Message.EnrichmentType.MSISDN;
  }

  @Override
  public Message enrich(Message message) {
    Message resultMessage = new Message(message.content, message.enrichmentType);

    String msisdn = resultMessage.content.get("msisdn");
    if (msisdn != null) {
      User user = userRepository.findByMsisdn(msisdn);
      if (user != null) {
        resultMessage.updateContent("firstName", user.firstName());
        resultMessage.updateContent("lastName", user.lastName());
      }
    }

    return resultMessage;
  }
}
