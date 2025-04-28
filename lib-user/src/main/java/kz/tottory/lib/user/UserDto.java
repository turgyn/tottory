package kz.tottory.lib.user;

import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String email;
    private String fullName;
    private boolean active;
}