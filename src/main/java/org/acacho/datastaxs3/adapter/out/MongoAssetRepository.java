package org.acacho.datastaxs3.adapter.out;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acacho.datastaxs3.adapter.out.mapper.MongoAssetDtoMapper;
import org.acacho.datastaxs3.domain.entity.Asset;
import org.acacho.datastaxs3.domain.repository.AssetRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoAssetRepository implements AssetRepository {

  private final SpringMongoAssetRepository springMongoAssetRepository;

  public Optional<Asset> findById(String key) {
    var mongoAssetDtoOptional = springMongoAssetRepository.findById(key);
    if (mongoAssetDtoOptional.isPresent()) {
      log.info("Asset with id {} retrieved successfully", key);
      var mongoAssetDto = mongoAssetDtoOptional.get();
      return Optional.of(MongoAssetDtoMapper.fromMongoAssetDtoToAsset(mongoAssetDto));
    }
    log.info("Asset with id {} could not be found", key);
    return Optional.empty();
  }

  public Asset save(Asset asset) {
    var savedMongoAsset = springMongoAssetRepository
        .save(MongoAssetDtoMapper.fromAssetToMongoAssetDto(asset));
    log.info("Asset with id {} saved successfully", asset.getId());
    return MongoAssetDtoMapper.fromMongoAssetDtoToAsset(savedMongoAsset);
  }
}
