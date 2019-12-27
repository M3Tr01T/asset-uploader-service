package org.acacho.datastaxs3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import org.acacho.datastaxs3.adapter.out.S3UrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.http.SdkHttpRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@ExtendWith(MockitoExtension.class)
class S3UrlServiceTest {

  @Mock
  private S3Presigner presigner;
  @InjectMocks
  private S3UrlService s3UrlService;

  @Test
  void createSignedUrlForUpload() throws URISyntaxException, MalformedURLException {
    URI fakeUrl = new URI("http://fake.com");
    SdkHttpRequest sdkHttpRequest = SdkHttpRequest.builder()
        .uri(fakeUrl)
        .protocol("http")
        .method(SdkHttpMethod.POST)
        .build();
    PresignedPutObjectRequest presignedPutObjectRequest = PresignedPutObjectRequest.builder()
        .httpRequest(sdkHttpRequest)
        .expiration(Instant.now())
        .isBrowserExecutable(false)
        .signedHeaders(Collections.singletonMap("test", Collections.singletonList("test")))
        .build();
    when(presigner.presignPutObject(any(PutObjectPresignRequest.class)))
        .thenReturn(presignedPutObjectRequest);
    var url = s3UrlService.retrieveUrlForUpload("key");
    assertEquals(fakeUrl.toURL(), url);
  }

  @Test
  void createSignedUrlForDownload() throws URISyntaxException, MalformedURLException {
    URI fakeUrl = new URI("http://fake.com");
    SdkHttpRequest sdkHttpRequest = SdkHttpRequest.builder()
        .uri(fakeUrl)
        .protocol("http")
        .method(SdkHttpMethod.POST)
        .build();
    PresignedGetObjectRequest presignedGetObjectRequest = PresignedGetObjectRequest.builder()
        .httpRequest(sdkHttpRequest)
        .expiration(Instant.now())
        .isBrowserExecutable(false)
        .signedHeaders(Collections.singletonMap("test", Collections.singletonList("test")))
        .build();
    when(presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .thenReturn(presignedGetObjectRequest);
    var url = s3UrlService.retrieveUrlForDownload("key", 55);
    assertEquals(fakeUrl.toURL(), url);
  }
}