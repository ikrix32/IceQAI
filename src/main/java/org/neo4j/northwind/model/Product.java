package org.neo4j.northwind.model;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Entity class to represent a Product entity within the Northwind example dataset.
 * 
 * @author Axel Morgner
 */
@NodeEntity
public class Product {
	
	@GraphId
	public Long id;
	
	public String productID;
	public String productName;
	public String supplierID;
	public String categoryID;
	public String quantityPerUnit;
	public Double unitPrice;
	public Integer unitsInStock;
	public Integer unitsOnOrder;
	public Integer reorderLevel;
	public Boolean discontinued;
	
	@Relationship(type = "ORDERS", direction = Relationship.INCOMING)
	public Order order;
	@Relationship(type = "SUPPLIES", direction = Relationship.INCOMING)
	public Supplier supplier;
	@Relationship(type = "PART_OF", direction = Relationship.OUTGOING)
	public Category category;
	
	public Product() {
	}
	
	public Product(Long id, String productID, String productName, String supplierID, String categoryID, String quantityPerUnit, Double unitPrice, Integer unitsInStock, Integer unitsOnOrder, Integer reorderLevel, Boolean discontinued) {
		this.id = id;
		this.productID = productID;
		this.productName = productName;
		this.supplierID = supplierID;
		this.categoryID = categoryID;
		this.quantityPerUnit = quantityPerUnit;
		this.unitPrice = unitPrice;
		this.unitsInStock = unitsInStock;
		this.unitsOnOrder = unitsOnOrder;
		this.reorderLevel = reorderLevel;
		this.discontinued = discontinued;
	}
	
}
