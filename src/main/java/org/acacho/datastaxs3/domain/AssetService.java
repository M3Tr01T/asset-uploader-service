package org.acacho.datastaxs3.domain;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acacho.datastaxs3.domain.entity.Asset;
import org.acacho.datastaxs3.domain.exception.AssetNotFoundException;
import org.acacho.datastaxs3.domain.repository.AssetRepository;
import org.acacho.datastaxs3.domain.valueobject.AssetStatus;

@Slf4j
@RequiredArgsConstructor
public class AssetService {

  private final UrlService urlService;
  private final AssetRepository assetRepository;

  /**
   * Creates a new Asset and returns it.
   *
   * @return new Asset created
   */
  public Asset createAsset() {
    var id = UUID.randomUUID().toString();
    var url = urlService.retrieveUrlForUpload(id);
    var asset = Asset.builder()
        .id(id)
        .uploadUrl(url)
        .status(AssetStatus.CREATED)
        .build();

    assetRepository.save(asset);
    return asset;
  }

  /**
   * Updates the status of an Asset if it exists.
   *
   * @param id        id of the asset to update.
   * @param newStatus new status to be set.
   * @return the asset modified.
   * @throws AssetNotFoundException if the asset with the specified id cannot be found.
   */
  public Asset updateAssetStatus(String id, AssetStatus newStatus) throws AssetNotFoundException {
    if (newStatus != AssetStatus.UPLOADED) {
      var errorMsg = "Only status \"uploaded\" is supported";
      throw new IllegalArgumentException(errorMsg);
    }

    var assetOptional = assetRepository.findById(id);
    if (assetOptional.isEmpty()) {
      throw new AssetNotFoundException("Asset not found with id " + id);
    }
    var asset = assetOptional.get();
    asset.setStatus(newStatus);
    assetRepository.save(asset);
    return asset;
  }

  /**
   * Returns and asset with a new URL that allows to download it.
   *
   * @param id      id of the asset to update.
   * @param timeout timeout for the download link to be alive.
   * @return modified asset.
   * @throws AssetNotFoundException if the asset with the specified id cannot be found.
   */
  public Asset updateAssetWithDownloadUrl(String id, int timeout) throws AssetNotFoundException {
    var assetOptional = assetRepository.findById(id);
    if (assetOptional.isEmpty()) {
      throw new AssetNotFoundException("Asset not found with id " + id);
    }
    var asset = assetOptional.get();
    var assetStatus = asset.getStatus();
    if (assetStatus != AssetStatus.UPLOADED) {
      var errorMsg = "Asset with specified id has not status \"uploaded\"";
      throw new IllegalArgumentException(errorMsg);
    }
    var signedUrl = urlService.retrieveUrlForDownload(id, timeout);
    asset.setDownloadUrl(signedUrl);
    assetRepository.save(asset);
    return asset;
  }
}
