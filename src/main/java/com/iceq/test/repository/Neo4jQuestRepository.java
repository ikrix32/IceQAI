package com.iceq.test.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import com.iceq.test.model.Neo4jQuest;

@Repository
public interface Neo4jQuestRepository extends GraphRepository<Neo4jQuest>{
	Neo4jQuest findById(Long id);
}
