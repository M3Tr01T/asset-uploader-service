package org.acacho.datastaxs3.adapter.out.dto;

import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.acacho.datastaxs3.domain.valueobject.AssetStatus;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
public class MongoAssetDto {

  @Id
  private String id;
  private AssetStatus status;
  private URL downloadUrl;
  private URL uploadUrl;
}
