package com.iceq.springdata.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.iceq.springdata.model.Node;

@Repository
public interface NodeRepository extends GraphRepository<Node>
{	
	@Query("MATCH (n:TestNode) RETURN n")
	public Iterable<Node> findTestNodes();
}