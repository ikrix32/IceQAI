package com.iceq.test.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Neo4jPlayer {
	@GraphId
	private Long gid;
	@Index
	private Long id;
	
	private String nickname;
	
	@Relationship(type="IS_PLAYING", direction=Relationship.OUTGOING)
	private Neo4jGame currentGame;

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Neo4jGame getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(Neo4jGame currentGame) {
		this.currentGame = currentGame;
	}
}
