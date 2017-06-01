package com.iceq.test.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iceq.test.model.Neo4jGameGroup;

@Repository
public interface Neo4jGameGroupRepository  extends GraphRepository<Neo4jGameGroup>{
	Neo4jGameGroup findById(Long id);
	
	@Query("MATCH (n:Neo4jQuest{id:{quest}})<-[:ON_QUEST]-(m:Open) RETURN m")
	List<Neo4jGameGroup> findOpenGroups(@Param("quest") Long quest);
}
