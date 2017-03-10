package org.neo4j.northwind.model;

import java.util.HashSet;
import java.util.Set;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Entity class to represent a Category entity within the Northwind example dataset.
 * 
 * @author Axel Morgner
 */
@NodeEntity
public class Category {
	
	@GraphId
	public Long id;
	
	public String categoryName;
	public String description;
	public String picture;
	public String categoryID;
	
	@Relationship(type = "PART_OF", direction = Relationship.INCOMING)
	public Set<Product> products;
	
	public Category() {
		products = new HashSet<>();
	}

	public Category(Long id, String categoryName, String description, String picture, String categoryID) {
		this.id = id;
		this.categoryName = categoryName;
		this.description = description;
		this.picture = picture;
		this.categoryID = categoryID;
		
	}
	
}
