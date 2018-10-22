package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class User {
  private String firstName;
  private String lastName;
  private String userEmail;
  private String password;
}
