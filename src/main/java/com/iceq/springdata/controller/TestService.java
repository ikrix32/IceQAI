package com.iceq.springdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iceq.springdata.model.Node;
import com.iceq.springdata.repository.NodeRepository;

@Service
public class TestService
{
	@Autowired
	NodeRepository nodeRepository;

	@Transactional
	public void runTestRelations() {
		//insert node 1
		Node n1 = new Node();
		n1.setName("N1");
		n1.addLabel("Source");
		nodeRepository.save(n1);
		
		Node n2 = new Node();		
		n2.setName("N2");
		n2.addLabel("Destination");
		nodeRepository.save(n2);
		
		Node n3 = new Node();		
		n3.setName("N3");
		n3.addLabel("Destination");
		nodeRepository.save(n2);
		
		n1.addRelation("Egal", n2);
		n1.addRelation("Are", n3);
		nodeRepository.save(n1);
		//nodeRepository.delete(n);
		
		/*for (final Node node : nodeRepository.findAll()) {	
			System.out.print("\nNode "+node.name+" has labels: ");
			for (final String label : node.getLabels())
				System.out.print(label+",");
		}
		
		for (final Node r : nodeRepository.findTestNodes()) {	
			System.out.print("\nNode "+r.name+" has TestNode label");
		}*/
	}
	
	@Transactional
	public void runTest() {
		//insert node 1
		Node n = nodeRepository.findOne(1L);
		//n.setName("TestLabels");
		n.addLabel("TestNode2");
		nodeRepository.save(n);
		
		//nodeRepository.delete(n);
		
		for (final Node node : nodeRepository.findAll()) {	
			System.out.print("\nNode "+node.name+" has labels: ");
			for (final String label : node.getLabels())
				System.out.print(label+",");
		}
		
		for (final Node r : nodeRepository.findTestNodes()) {	
			System.out.print("\nNode "+r.name+" has TestNode label");
		}
	}
}
