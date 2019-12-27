package org.acacho.datastaxs3.adapter.in.mapper;

import lombok.experimental.UtilityClass;
import org.acacho.datastaxs3.adapter.in.dto.DownloadUrlDto;
import org.acacho.datastaxs3.domain.entity.Asset;

@UtilityClass
public class DownloadUrlDtoMapper {

  public static DownloadUrlDto fromAssetToDownloadUrlDto(Asset asset) {
    return DownloadUrlDto.builder()
        .signedUrl(asset.getDownloadUrl())
        .build();
  }
}
