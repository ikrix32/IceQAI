package org.neo4j.northwind.model;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
public class ProductNode
{
	Product product;
	List<String> labels;
	
	public Product getProduct(){
		return product;
	}
	
	public List<String> getLabels(){
		return labels;
	}
}
