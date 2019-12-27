package org.acacho.datastaxs3.boot.config;

import org.acacho.datastaxs3.domain.AssetService;
import org.acacho.datastaxs3.domain.UrlService;
import org.acacho.datastaxs3.domain.repository.AssetRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultServiceConfig {

  @Bean
  public AssetService assetService(UrlService urlService, AssetRepository assetRepository) {
    return new AssetService(urlService, assetRepository);
  }
}
