package org.neo4j.northwind.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;
import org.neo4j.northwind.model.Product;
import org.neo4j.northwind.model.ProductNode;
import org.springframework.data.neo4j.annotation.Query;

/**
 * Repository interface for products.
 *
 * @author Axel Morgner
*/
@Repository
public interface ProductRepository extends GraphRepository<Product> {

	@Query("MATCH (p:Product) RETURN p LIMIT 100")
	public Iterable<Product> findAllLimit100();

	@Query("MATCH (p:Product) WHERE p.productName =~ {0} RETURN p LIMIT 1")
	public Product findByName(String name);

	@Query("MATCH (p:Product) WHERE p.productName =~ {0} RETURN labels(p) as labels,p as product")
	public ProductNode findProductNode(String name);
}
