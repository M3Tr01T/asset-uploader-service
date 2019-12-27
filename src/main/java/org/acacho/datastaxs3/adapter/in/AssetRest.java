package org.acacho.datastaxs3.adapter.in;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static software.amazon.awssdk.http.HttpStatusCode.ACCEPTED;
import static software.amazon.awssdk.http.HttpStatusCode.BAD_REQUEST;
import static software.amazon.awssdk.http.HttpStatusCode.CREATED;
import static software.amazon.awssdk.http.HttpStatusCode.INTERNAL_SERVER_ERROR;
import static software.amazon.awssdk.http.HttpStatusCode.NOT_FOUND;
import static software.amazon.awssdk.http.HttpStatusCode.OK;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.acacho.datastaxs3.adapter.in.dto.DownloadUrlDto;
import org.acacho.datastaxs3.adapter.in.dto.StatusDto;
import org.acacho.datastaxs3.adapter.in.dto.UploadUrlDto;
import org.acacho.datastaxs3.domain.exception.AssetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(tags = {"asset"}, value = "/asset")
public interface AssetRest {

  @ApiOperation(value = "Create asset and S3 presigned upload URL")
  @ApiResponses(value = {
      @ApiResponse(code = CREATED, message = "Asset created", response = String.class),
      @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "Internal Server Error",
          response = String.class)})
  @PostMapping(path = "/asset", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  UploadUrlDto createUploadSignedUrl();

  @ApiOperation(value = "Update asset status")
  @ApiResponses(value = {
      @ApiResponse(code = ACCEPTED, message = "Asset updated", response = String.class),
      @ApiResponse(code = BAD_REQUEST, message = "Bad request", response = String.class),
      @ApiResponse(code = NOT_FOUND, message = "Asset not found", response = String.class),
      @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "Internal Server Error",
          response = String.class)})
  @PutMapping(path = "/asset/{assetId}", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.ACCEPTED)
  String updateAssetStatus(@PathVariable final String assetId,
      @RequestBody StatusDto status) throws AssetNotFoundException;

  @ApiOperation(value = "Generate S3 presigned download link for asset")
  @ApiResponses(value = {
      @ApiResponse(code = OK, message = "Generated download link for asset"),
      @ApiResponse(code = BAD_REQUEST, message = "Bad request", response = String.class),
      @ApiResponse(code = NOT_FOUND, message = "Asset not found", response = String.class),
      @ApiResponse(code = INTERNAL_SERVER_ERROR, message = "Internal Server Error",
          response = String.class)})
  @GetMapping(path = "/asset/{assetId}", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  DownloadUrlDto retrieveDownloadSignedUrl(@PathVariable final String assetId,
      @RequestParam(required = false, defaultValue = "60") int timeout)
      throws AssetNotFoundException;
}
