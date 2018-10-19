package model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

  private String firstName;
  private String lastName;
  private String userEmail;
  private String password;
}
