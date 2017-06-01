package com.iceq.springdata.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="RELATION")
public class Relation {
	@GraphId   private Long id;
	@Property  private String name;
    @StartNode private Node source;
    @EndNode   private Node destination;
    
    //@Labels
   // private Collection<String> labels;
    
    public Relation(){}
    
    public Relation( String label, Node src, Node dest){
    	this.source = src;
    	this.destination = dest;
    	this.name = label;
    	//addLabel(label);
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
//	public void addLabel(String label){
//		if(labels == null)
//			labels = new ArrayList<>();
//		labels.add(label);
//	}
//	
//	public boolean hasLabel(String label){
//		if(labels == null)
//			return false;
//		
//		return labels.contains(label);
//	}
//	
//	public Collection<String> getLabels() {
//		return labels;
//	}
//
//	public void setLabels(Collection<String> labels) {
//		this.labels = labels;
//	}
}
