package org.acacho.datastaxs3.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DownloadUrlDto {

  @JsonProperty("Download_url")
  URL signedUrl;
}
