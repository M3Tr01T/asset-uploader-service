package org.acacho.datastaxs3.adapter.out;

import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acacho.datastaxs3.domain.UrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UrlService implements UrlService {

  private final S3Presigner presigner;

  @Value("${signatureDuration.upload:60}")
  long signatureDuration;

  @Value("${s3.bucketName:assetservicedatastax}")
  String bucketName;

  /**
   * Creates a signed URL to upload to Amazon S3 a file with the key passed as a parameter.
   *
   * @param key the key of the object for which the URL will be generated.
   * @return the signed URL of the S3 object.
   */
  @Override
  public URL retrieveUrlForUpload(String key) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

    PutObjectPresignRequest putObjectPresignRequest =
        PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(signatureDuration))
            .putObjectRequest(putObjectRequest)
            .build();

    PresignedPutObjectRequest presignedPutObjectRequest =
        presigner.presignPutObject(putObjectPresignRequest);

    log.info("Generated presigned URL for PUT: {}", presignedPutObjectRequest.url());
    return presignedPutObjectRequest.url();
  }

  /**
   * Creates a signed URL to download from Amazon S3 the file with the key passed as a parameter.
   *
   * @param key               the key of the object for which the URL will be generated.
   * @param signatureDuration the duration of the URL.
   * @return the URL for downloading the file.
   */
  @Override
  public URL retrieveUrlForDownload(String key, int signatureDuration) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

    GetObjectPresignRequest getObjectPresignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(signatureDuration))
            .getObjectRequest(getObjectRequest)
            .build();

    PresignedGetObjectRequest presignedGetObjectRequest =
        presigner.presignGetObject(getObjectPresignRequest);

    log.info("Generated presigned URL for GET: {}", presignedGetObjectRequest.url());
    return presignedGetObjectRequest.url();
  }
}
