package com.iceq.test.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.iceq.test.model.Neo4jGame;

@Repository
public interface Neo4jGameRepository extends GraphRepository<Neo4jGame>{
	Neo4jGame findById(Long id);
}
