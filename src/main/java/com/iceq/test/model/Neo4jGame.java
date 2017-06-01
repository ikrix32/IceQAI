package com.iceq.test.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Neo4jGame {
	@GraphId
	private Long gid;
	@Index
	private Long id;
	
	@Relationship(type="PART_OF", direction=Relationship.OUTGOING)
	private Neo4jGameGroup gameGroup;

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Neo4jGameGroup getGameGroup() {
		return gameGroup;
	}

	public void setGameGroup(Neo4jGameGroup gameGroup) {
		this.gameGroup = gameGroup;
	}
}
