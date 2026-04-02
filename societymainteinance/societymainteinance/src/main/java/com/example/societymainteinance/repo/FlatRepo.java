package com.example.societymainteinance.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.societymainteinance.entity.FlatEntity;

@Repository
public interface FlatRepo extends JpaRepository<FlatEntity, Long> {

    // Search by flat number or block
    List<FlatEntity> findByFlatNumberContainingIgnoreCaseOrBlockContainingIgnoreCase(String flatNumber, String block);
}