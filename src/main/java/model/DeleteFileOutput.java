package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteFileOutput {
  private String status;
}
