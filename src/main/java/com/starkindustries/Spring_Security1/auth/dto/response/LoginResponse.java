package com.starkindustries.Spring_Security1.auth.dto.response;

import com.starkindustries.Spring_Security1.auth.model.Users;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginResponse {

    private Users users;

    private String jwtToken;
}
