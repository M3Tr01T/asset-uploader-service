package org.acacho.datastaxs3.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import org.acacho.datastaxs3.adapter.in.dto.StatusDto;
import org.acacho.datastaxs3.adapter.out.MongoAssetRepository;
import org.acacho.datastaxs3.adapter.out.dto.MongoAssetDto;
import org.acacho.datastaxs3.boot.DatastaxS3Application;
import org.acacho.datastaxs3.domain.AssetService;
import org.acacho.datastaxs3.domain.entity.Asset;
import org.acacho.datastaxs3.domain.exception.AssetNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ContextConfiguration(classes = {DatastaxS3Application.class})
@WebMvcTest(AssetRest.class)
public class AssetRestTest {

  public static final String UPLOADED = "uploaded";
  public static final String PATH_ASSET_ID = "/asset/{assetId}";
  public static final String PATH_TIMEOUT = "/asset/{assetId}?timeout=50";
  public static final String URL_STRING = "http://fake.com";
  @Autowired
  private MockMvc mvc;
  @MockBean
  MongoAssetRepository mongoAssetRepository;
  @MockBean
  MongoRepository<MongoAssetDto, String> springMongoAssetRepository;
  @MockBean
  AssetService assetService;
  @Autowired
  ObjectMapper objectMapper;

  @Test
  public void createSignedUrl() throws Exception {
    var asset = Asset.builder().id("assetId").uploadUrl(new URL(URL_STRING)).build();
    when(assetService.createAsset()).thenReturn(asset);
    mvc.perform(MockMvcRequestBuilders
        .post("/asset")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.upload_url").isNotEmpty());
  }

  @Test
  public void updateAssetStatus() throws Exception {
    var statusDto = StatusDto.builder().status(UPLOADED).build();
    mvc.perform(MockMvcRequestBuilders
        .put(PATH_ASSET_ID, 1)
        .content(objectMapper.writeValueAsString(statusDto))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isAccepted());
  }

  @Test
  public void updateAssetStatusAssetNotFoundException() throws Exception {
    when(assetService.updateAssetStatus(any(), any())).thenThrow(AssetNotFoundException.class);
    var statusDto = StatusDto.builder().status(UPLOADED).build();
    mvc.perform(MockMvcRequestBuilders
        .put(PATH_ASSET_ID, 1)
        .content(objectMapper.writeValueAsString(statusDto))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateAssetStatusIllegalArgException() throws Exception {
    when(assetService.updateAssetStatus(any(), any())).thenThrow(IllegalArgumentException.class);
    var statusDto = StatusDto.builder().status(UPLOADED).build();
    mvc.perform(MockMvcRequestBuilders
        .put(PATH_ASSET_ID, 1)
        .content(objectMapper.writeValueAsString(statusDto))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateAssetStatusNoBody() throws Exception {
    mvc.perform(MockMvcRequestBuilders
        .put(PATH_ASSET_ID, 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void retrieveUrl() throws Exception {
    var asset = Asset.builder().downloadUrl(new URL(URL_STRING)).build();
    when(assetService.updateAssetWithDownloadUrl(anyString(), eq(50))).thenReturn(asset);
    mvc.perform(MockMvcRequestBuilders
        .get(PATH_TIMEOUT, 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.Download_url").value(URL_STRING));
  }

  @Test
  public void retrieveUrlNotFound() throws Exception {
    when(assetService.updateAssetWithDownloadUrl(anyString(), eq(60)))
        .thenThrow(AssetNotFoundException.class);
    mvc.perform(MockMvcRequestBuilders
        .get(PATH_ASSET_ID, 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void retrieveUrlIllegal() throws Exception {
    when(assetService.updateAssetWithDownloadUrl(anyString(), eq(50)))
        .thenThrow(IllegalArgumentException.class);
    mvc.perform(MockMvcRequestBuilders
        .get(PATH_TIMEOUT, 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void retrieveUrlOtherEx() throws Exception {
    when(assetService.updateAssetWithDownloadUrl(anyString(), eq(50)))
        .thenThrow(NullPointerException.class);
    mvc.perform(MockMvcRequestBuilders
        .get(PATH_TIMEOUT, 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void retrieveUrlBadTimeout() throws Exception {
    var asset = Asset.builder().downloadUrl(new URL(URL_STRING)).build();
    when(assetService.updateAssetWithDownloadUrl(anyString(), eq(50))).thenReturn(asset);
    mvc.perform(MockMvcRequestBuilders
        .get("/asset/{assetId}?timeout=wrong", 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
