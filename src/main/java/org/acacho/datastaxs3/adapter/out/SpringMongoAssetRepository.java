package org.acacho.datastaxs3.adapter.out;

import org.acacho.datastaxs3.adapter.out.dto.MongoAssetDto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringMongoAssetRepository extends MongoRepository<MongoAssetDto, String> {

}
