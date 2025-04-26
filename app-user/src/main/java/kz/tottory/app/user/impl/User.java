package kz.tottory.app.user.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String email;
    private String fullName;
    private boolean active;
}
