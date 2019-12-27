package org.acacho.datastaxs3.adapter.out;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.acacho.datastaxs3.adapter.out.dto.MongoAssetDto;
import org.acacho.datastaxs3.domain.entity.Asset;
import org.acacho.datastaxs3.domain.valueobject.AssetStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MongoAssetRepositoryTest {

  private static final String ASSET_ID = "ASSET_ID";
  private static final AssetStatus STATUS_UPLOADED = AssetStatus.UPLOADED;

  @Mock
  private SpringMongoAssetRepository springMongoAssetRepository;
  @InjectMocks
  private MongoAssetRepository mongoAssetRepository;

  @Test
  void findById() {
    var mongoAssetDto = MongoAssetDto.builder()
        .id(ASSET_ID)
        .status(STATUS_UPLOADED)
        .build();
    var expectedAsset = Asset.builder()
        .id(ASSET_ID)
        .status(STATUS_UPLOADED)
        .build();
    when(springMongoAssetRepository.findById(ASSET_ID)).thenReturn(Optional.of(mongoAssetDto));
    var asset = mongoAssetRepository.findById(ASSET_ID).get();
    assertEquals(expectedAsset, asset);
  }

  @Test
  void findByIdEmptyValue() {
    when(springMongoAssetRepository.findById(ASSET_ID)).thenReturn(Optional.empty());
    var returnedValue = mongoAssetRepository.findById(ASSET_ID);
    assertTrue(returnedValue.isEmpty());
  }

  @Test
  void save() {
    var mongoAssetDto = MongoAssetDto.builder()
        .id(ASSET_ID)
        .status(STATUS_UPLOADED)
        .build();
    var expectedAsset = Asset.builder()
        .id(ASSET_ID)
        .status(STATUS_UPLOADED)
        .build();
    when(springMongoAssetRepository.save(mongoAssetDto)).thenReturn(mongoAssetDto);
    var asset = mongoAssetRepository.save(expectedAsset);
    assertEquals(expectedAsset, asset);
  }
}