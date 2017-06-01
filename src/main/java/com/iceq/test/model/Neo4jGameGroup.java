package com.iceq.test.model;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Neo4jGameGroup {
	public enum State {
		Open, Locked, Full
	}

	@GraphId
	private Long gid;
	@Index
	private Long id;

	private int ahi;

	private int noGames;

	@Relationship(type = "ON_QUEST", direction = Relationship.OUTGOING)
	private Neo4jQuest quest;

	@Labels
	private List<String> labels = new ArrayList<String>();

	public Neo4jGameGroup() {
		setState(State.Open);
	}
	
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

	public Neo4jQuest getQuest() {
		return quest;
	}

	public void setQuest(Neo4jQuest quest) {
		this.quest = quest;
	}

	public void increment() {
		noGames++;
		if (noGames >= 5) {
			setState(State.Full);
		}
	}

	private void setState(State state) {
//		if (state == State.Open) {
//			if (!labels.contains(State.Open.toString())) {
//				labels.add(State.Open.toString());
//			}
//		} else {
//			if (labels.contains(State.Open.toString())) {
//				labels.remove(State.Open.toString());
//			}
//		}
	}

	public int getAhi() {
		return ahi;
	}

	public void setAhi(int ahi) {
		this.ahi = ahi;
	}
}
