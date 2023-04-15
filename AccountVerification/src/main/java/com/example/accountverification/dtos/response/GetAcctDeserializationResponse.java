package com.example.accountverification.dtos.response;

import com.example.accountverification.data.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAcctDeserializationResponse {
    private Data data;
//    private boolean status;
//    private String message;
//    private String bank_id;
}
