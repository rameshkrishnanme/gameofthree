package com.app.gameofthree.controller;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.gameofthree.websocket.GameOfThreeSocketEndpoint;

@RestController
public class GamePlayerRestController {

	@GetMapping("/users")
	public Set<String> playerList() {
		
		Map<String, String> users = GameOfThreeSocketEndpoint.getUsers();
		Collection<String> values = users.values();
		
		Set<String> userSet = new HashSet<>(values);
		return userSet;
		
	}
	
}
