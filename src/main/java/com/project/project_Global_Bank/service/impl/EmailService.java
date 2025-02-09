package com.project.project_Global_Bank.service.impl;

import com.project.project_Global_Bank.dto.EmailDetails;

public interface EmailService
{
    void sendEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}
