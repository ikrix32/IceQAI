package com.iceq.springdata.model;

import java.util.ArrayList;
import java.util.Collection;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Node
{
	@GraphId
	public Long id;
	
	public String name;
	
	@Labels
    private Collection<String> labels;
	
	@Relationship
	private Collection<Node> relations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addLabel(String label){
		if(labels == null)
			labels = new ArrayList<>();
		labels.add(label);
	}
	
	public boolean hasLabel(String label){
		if(labels == null)
			return false;
		
		return labels.contains(label);
	}
	
	public Collection<String> getLabels() {
		return labels;
	}

	public void setLabels(Collection<String> labels) {
		this.labels = labels;
	}
}
