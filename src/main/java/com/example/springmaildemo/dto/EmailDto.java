package com.example.springmaildemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmailDto {

    private List<Recipient> recipients;
    private String subject;
    private String body;
    private List<String> ccMailIds;
    private List<String> bccMailIds;

    @Data
    private static class Recipient {
        private String recipientMailId;
        private String recipientName;
    }

    
}
