package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorModel {
  private String exceptionMsg;
}
