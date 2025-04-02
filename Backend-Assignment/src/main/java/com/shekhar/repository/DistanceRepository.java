package com.shekhar.repository;

import com.shekhar.model.DistanceEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DistanceRepository extends ListCrudRepository<DistanceEntity, Long> {
    List<DistanceEntity> findByOriginPincodeAndDestinationPincode(String originPincode, String destinationPincode);
}
