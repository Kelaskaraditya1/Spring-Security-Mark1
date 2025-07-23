package com.starkindustries.Spring_Security1.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginDto {

    private String username;

    private String password;
}
