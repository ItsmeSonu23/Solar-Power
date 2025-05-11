package com.solar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Queries {
    private String querySubject;
    private String queryPhoneNumber;
    private String queryEmail;
    private String queryDescription;
    private QueryStatus status;
}
