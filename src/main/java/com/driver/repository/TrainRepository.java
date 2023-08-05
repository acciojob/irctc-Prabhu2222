package com.driver.repository;

import com.driver.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainRepository extends JpaRepository<Train,Integer> {

    @Query(value="select * from trains where train_id=:trainId",nativeQuery = true)
    Train findByTrainId(Integer trainId);
}
