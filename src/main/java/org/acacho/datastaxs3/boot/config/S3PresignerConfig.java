package org.acacho.datastaxs3.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3PresignerConfig {

  @Value("${s3.accessKey}")
  private String accessKey;
  @Value("${s3.secretKey}")
  private String secretKey;
  @Value("${s3.region}")
  private String region;

  @Bean
  public S3Presigner createS3Presigner() {
    var awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Presigner.builder()
        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
        .region(Region.of(region))
        .build();
  }
}
