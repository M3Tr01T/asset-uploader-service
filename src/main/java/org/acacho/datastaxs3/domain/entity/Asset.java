package org.acacho.datastaxs3.domain.entity;

import java.net.URL;
import lombok.Builder;
import lombok.Data;
import org.acacho.datastaxs3.domain.valueobject.AssetStatus;

@Data
@Builder
public class Asset {

  private final String id;
  private AssetStatus status;
  private URL downloadUrl;
  private URL uploadUrl;
}
