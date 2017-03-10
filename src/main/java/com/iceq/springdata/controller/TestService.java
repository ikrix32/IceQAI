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
