package org.neo4j.northwind.model;

import java.util.HashSet;
import java.util.Set;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * Entity class to represent a Supplier entity within the Northwind example dataset.
 * 
 * @author Axel Morgner
 */
@NodeEntity
public class Supplier {
	
	@GraphId
	public Long id;
	
	public String supplierID;
	public String contactTitle;
	public String contactName;
	public String homePage;
	public String city;
	public String postalCode;
	public String country;
	public String phone;
	public String fax;
	public String companyName;
	public String region;
	public String address;

	@Relationship(type = "SUPPLIES", direction = Relationship.OUTGOING)
	public Set<Product> products;
	
	public Supplier() {
		products = new HashSet<>();
	}

	public Supplier(Long id, String supplierID, String contactTitle, String contactName, String homePage, String city, String postalCode, String country, String phone, String fax, String companyName, String region, String address) {
		this.id = id;
		this.supplierID = supplierID;
		this.contactTitle = contactTitle;
		this.contactName = contactName;
		this.homePage = homePage;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.phone = phone;
		this.fax = fax;
		this.companyName = companyName;
		this.region = region;
		this.address = address;
	}
	
}
