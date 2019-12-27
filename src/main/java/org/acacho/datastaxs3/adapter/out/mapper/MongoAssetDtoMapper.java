package org.acacho.datastaxs3.adapter.out.mapper;

import lombok.experimental.UtilityClass;
import org.acacho.datastaxs3.adapter.out.dto.MongoAssetDto;
import org.acacho.datastaxs3.domain.entity.Asset;

@UtilityClass
public class MongoAssetDtoMapper {

  public static Asset fromMongoAssetDtoToAsset(MongoAssetDto mongoAssetDto) {
    return Asset.builder()
        .status(mongoAssetDto.getStatus())
        .id(mongoAssetDto.getId())
        .build();
  }

  public static MongoAssetDto fromAssetToMongoAssetDto(Asset asset) {
    return MongoAssetDto.builder()
        .status(asset.getStatus())
        .id(asset.getId())
        .build();
  }
}
