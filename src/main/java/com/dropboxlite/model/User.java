package com.dropboxlite.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private int userId;
  private String firstName;
  private String lastName;
  private String userEmail;
  private String password;
  private boolean adminUser;
}
