package com.evampsanga.assignment.responses;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class ResponseVO {

        private String uuid;
        private String fname;
        private List<String> errors;
        private List<Payload> payload;
}
