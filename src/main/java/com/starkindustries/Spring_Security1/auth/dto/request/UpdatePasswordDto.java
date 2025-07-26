package com.starkindustries.Spring_Security1.auth.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdatePasswordDto {

    private String userId;
    private String currentPassword;
    private String newPassword;
}
