package org.acacho.datastaxs3.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadUrlDto {

  String id;

  @JsonProperty("upload_url")
  URL signedUrl;
}
