package com.jasmine.dev.CommonService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private  String userId;
    private String firstName;
    private String lastName;
    private CardDTO cardDTO;
}
