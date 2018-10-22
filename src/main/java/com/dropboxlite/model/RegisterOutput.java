package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class RegisterOutput {
  private boolean userCreated;
  private int userId;
}