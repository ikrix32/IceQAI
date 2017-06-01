package com.iceq.test.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iceq.test.model.Neo4jGame;
import com.iceq.test.model.Neo4jGameGroup;
import com.iceq.test.model.Neo4jPlayer;
import com.iceq.test.model.Neo4jQuest;
import com.iceq.test.repository.Neo4jGameGroupRepository;
import com.iceq.test.repository.Neo4jGameRepository;
import com.iceq.test.repository.Neo4jPlayerRepository;
import com.iceq.test.repository.Neo4jQuestRepository;

@Service
public class FitkinTestService {

	@Autowired
	Neo4jPlayerRepository playerRepository;
	
	@Autowired
	Neo4jGameRepository gameRepository;
	
	@Autowired
	Neo4jGameGroupRepository gameGroupRepository;
	
	@Autowired
	Neo4jQuestRepository questRepository;
	
	private long groupIndex;
	private long gameIndex;
	
	public void runTest() throws Exception {
		int noPlayers = 3000;
		
		long startTime = System.currentTimeMillis();
		List<Neo4jGameGroup> openGroups = gameGroupRepository.findOpenGroups(1L);
//		for(int i = 0; i < noPlayers; i++) {
//			Neo4jPlayer player = createPlayer(i);
//			play(player);
//		}
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Test duration:"+duration+"ms avg:"+(duration / noPlayers)+"ms");
	}
	
	@Transactional
	private void play(Neo4jPlayer player) throws Exception {
		int questId = (int)(20 * Math.random());
		
		Neo4jQuest quest = getOrCreateQuest(questId);
		List<Neo4jGameGroup> openGroups = gameGroupRepository.findOpenGroups(quest.getId());
		long gIndex = (int)(groupIndex * Math.random());
		
		Neo4jGame game = null;
		if(gIndex < 1) {
			game = createGroup(quest);
		}else {
			game = joinGroup(gIndex);
		}
		player.setCurrentGame(game);
		playerRepository.save(player);
	}
	
	@Transactional
	private Neo4jGame createGroup(Neo4jQuest quest) throws Exception {
		Neo4jGameGroup group = new Neo4jGameGroup();
		group.setId(nextGroupId());
		group.setQuest(quest);
		group.setAhi(Math.round((float)(20 * Math.random())));
		
		group.increment();
		gameGroupRepository.save(group);
		
		Neo4jGame game = new Neo4jGame();
		game.setId(nextGameId());
		game.setGameGroup(group);
		
		return game;
	}
	
	@Transactional
	private Neo4jGame joinGroup(Long id) throws Exception {
		Neo4jGameGroup group = gameGroupRepository.findById(id);
		group.increment();
		gameGroupRepository.save(group);
		
		Neo4jGame game = new Neo4jGame();
		game.setId(nextGameId());
		game.setGameGroup(group);
		
		gameRepository.save(game);
		
		return game;
	}
	
	@Transactional
	private Neo4jQuest getOrCreateQuest(long id) {
		Neo4jQuest quest = questRepository.findById(id);
		
		if(quest == null) {
			quest = new Neo4jQuest();
			quest.setId(id);
			quest.setName("Q"+id);
		}
		
		return quest;
	}
	
	@Transactional
	private Neo4jPlayer createPlayer(long id) {
		Neo4jPlayer player = new Neo4jPlayer();
		player.setId(id);
		player.setNickname("P"+id);
		
		playerRepository.save(player);
		
		return player;
	}
	
	private long nextGroupId() {
		groupIndex++;
		return groupIndex;
	}
	
	private long nextGameId() {
		gameIndex++;
		return gameIndex;
	}
}
