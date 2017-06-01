package com.iceq.test.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.iceq.test.model.Neo4jPlayer;

@Repository
public interface Neo4jPlayerRepository extends GraphRepository<Neo4jPlayer>{
	Neo4jPlayer findById(Long id);
}
