package com.devfreitag.pismotest.repositories;

import com.devfreitag.pismotest.entities.OperationType;
import com.devfreitag.pismotest.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {


}
