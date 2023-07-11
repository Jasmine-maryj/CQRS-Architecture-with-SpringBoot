package com.jasmine.dev.UserService.projections;

import com.jasmine.dev.CommonService.dto.CardDTO;
import com.jasmine.dev.CommonService.dto.UserDTO;
import com.jasmine.dev.CommonService.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public UserDTO getUserPaymentDetails(GetUserPaymentDetailsQuery getUserPaymentDetailsQuery){
        CardDTO cardDTO = CardDTO.builder()
                .name("Jay")
                .validUntilMonth(12)
                .validUntilYear(2025)
                .cardNumber("123456754321")
                .cvv(777)
                .build();

        return UserDTO.builder()
                .userId(getUserPaymentDetailsQuery.getUserId())
                .firstName("Jay")
                .lastName("Mary")
                .cardDTO(cardDTO)
                .build();
    }
}
