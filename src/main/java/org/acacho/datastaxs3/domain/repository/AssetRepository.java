package org.acacho.datastaxs3.domain.repository;

import java.util.Optional;
import org.acacho.datastaxs3.domain.entity.Asset;

public interface AssetRepository {

  Optional<Asset> findById(String key);

  Asset save(Asset asset);
}
