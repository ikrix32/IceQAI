package com.iceq.springdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iceq.natural.language.Sequence;
import com.iceq.springdata.repository.NodeRepository;

@Service
public class MemService {
	@Autowired
	NodeRepository nodeRepository;
	
	@Transactional
	public void saveStatement(Sequence seq){
		
	}
}
