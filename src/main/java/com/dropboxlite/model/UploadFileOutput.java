package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFileOutput {
  private boolean result;
}
