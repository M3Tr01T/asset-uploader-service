package org.acacho.datastaxs3.adapter.in.mapper;

import lombok.experimental.UtilityClass;
import org.acacho.datastaxs3.adapter.in.dto.UploadUrlDto;
import org.acacho.datastaxs3.domain.entity.Asset;

@UtilityClass
public class UploadUrlDtoMapper {

  public static UploadUrlDto fromAssetToUploadUrlDto(Asset asset) {
    return UploadUrlDto.builder()
        .id(asset.getId())
        .signedUrl(asset.getUploadUrl())
        .build();
  }
}
