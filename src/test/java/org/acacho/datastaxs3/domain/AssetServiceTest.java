package org.acacho.datastaxs3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.acacho.datastaxs3.domain.entity.Asset;
import org.acacho.datastaxs3.domain.exception.AssetNotFoundException;
import org.acacho.datastaxs3.domain.repository.AssetRepository;
import org.acacho.datastaxs3.domain.valueobject.AssetStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

  private static final String ASSET_ID = "ASSET_ID";
  private static final AssetStatus STATUS_UPLOADED = AssetStatus.UPLOADED;
  private static final int TIMEOUT = 60;

  @Mock
  private UrlService urlService;
  @Mock
  private AssetRepository assetRepository;
  @InjectMocks
  private AssetService assetService;

  @Test
  void createAsset() {
    var asset = assetService.createAsset();
    assertNotNull(asset);
    assertNotNull(asset.getId());
  }

  @Test
  void updateAssetStatus() throws AssetNotFoundException {
    var asset = Asset.builder()
        .id(ASSET_ID)
        .build();
    when(assetRepository.findById(ASSET_ID)).thenReturn(Optional.of(asset));
    asset = assetService.updateAssetStatus(ASSET_ID, STATUS_UPLOADED);
    assertNotNull(asset);
    assertNotNull(asset.getStatus());
    assertEquals(STATUS_UPLOADED, asset.getStatus());
  }

  @Test
  void updateAssetStatusBadStatusThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> {
      assetService.updateAssetStatus(ASSET_ID, AssetStatus.CREATED);
    });
  }

  @Test
  void updateAssetStatusNotExistingAsset() {
    assertThrows(AssetNotFoundException.class, () -> {
      assetService.updateAssetStatus(ASSET_ID, STATUS_UPLOADED);
    });
  }

  @Test
  void updateAssetWithDownloadUrl() throws AssetNotFoundException {
    var asset = Asset.builder()
        .id(ASSET_ID)
        .status(STATUS_UPLOADED)
        .build();
    when(assetRepository.findById(ASSET_ID)).thenReturn(Optional.of(asset));
    asset = assetService.updateAssetWithDownloadUrl(ASSET_ID, TIMEOUT);
    assertNotNull(asset);
    assertNotNull(asset.getStatus());
    assertEquals(STATUS_UPLOADED, asset.getStatus());
  }

  @Test
  void updateAssetWithDownloadUrlBadStatusThrowsException() {
    var asset = Asset.builder()
        .id(ASSET_ID)
        .build();
    when(assetRepository.findById(ASSET_ID)).thenReturn(Optional.of(asset));
    assertThrows(IllegalArgumentException.class, () -> {
      assetService.updateAssetWithDownloadUrl(ASSET_ID, TIMEOUT);
    });
  }

  @Test
  void updateAssetWithDownloadUrlNotExistingAsset() {
    assertThrows(AssetNotFoundException.class, () -> {
      assetService.updateAssetWithDownloadUrl(ASSET_ID, TIMEOUT);
    });
  }
}