package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;

public interface MailService {
    void sendCustomerCredentials(String mail, String username, String password, String realmUrl) throws ApplicationException;
}
