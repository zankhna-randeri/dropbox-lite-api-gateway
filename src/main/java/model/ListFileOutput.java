package model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import model.S3File;

import java.util.List;

@Data
@Builder
@ToString
public class ListFileOutput {
  private List<S3File> files;
}