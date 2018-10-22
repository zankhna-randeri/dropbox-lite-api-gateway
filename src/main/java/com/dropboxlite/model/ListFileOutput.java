package com.dropboxlite.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class ListFileOutput {
  private List<FileInfo> files;
}