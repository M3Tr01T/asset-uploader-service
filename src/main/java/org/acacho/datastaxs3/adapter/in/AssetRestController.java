package org.acacho.datastaxs3.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acacho.datastaxs3.adapter.in.dto.DownloadUrlDto;
import org.acacho.datastaxs3.adapter.in.dto.StatusDto;
import org.acacho.datastaxs3.adapter.in.dto.UploadUrlDto;
import org.acacho.datastaxs3.adapter.in.mapper.DownloadUrlDtoMapper;
import org.acacho.datastaxs3.adapter.in.mapper.UploadUrlDtoMapper;
import org.acacho.datastaxs3.domain.AssetService;
import org.acacho.datastaxs3.domain.exception.AssetNotFoundException;
import org.acacho.datastaxs3.domain.valueobject.AssetStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/")
public class AssetRestController implements AssetRest {

  private final AssetService assetService;

  @Override
  public UploadUrlDto createUploadSignedUrl() {
    log.info("Received POST call to /asset");
    var asset = assetService.createAsset();
    return UploadUrlDtoMapper.fromAssetToUploadUrlDto(asset);
  }

  @Override
  public String updateAssetStatus(final String assetId, StatusDto status)
      throws AssetNotFoundException {
    log.info("Received PUT call to /asset/{}", assetId);
    assetService.updateAssetStatus(assetId, AssetStatus.valueOf(status.getStatus().toUpperCase()));
    return "Asset with id " + assetId + " updated successfully with status \"" + status.getStatus()
        + "\"";
  }

  @Override
  public DownloadUrlDto retrieveDownloadSignedUrl(final String assetId, int timeout)
      throws AssetNotFoundException {
    log.info("Received GET call to /asset/{}", assetId);
    var asset = assetService.updateAssetWithDownloadUrl(assetId, timeout);
    return DownloadUrlDtoMapper.fromAssetToDownloadUrlDto(asset);
  }
}
