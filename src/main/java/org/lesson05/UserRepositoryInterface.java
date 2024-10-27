package org.lesson05;

public interface UserRepositoryInterface {
  User findByMsisdn(String msisdn);
  void updateUserByMsisdn(String msisdn, User user);
}
