package com.app.gameofthree.model;

import com.app.gameofthree.websocket.GameOfThreeSocketEndpoint;

public class Playground {

	private String name;
	
	private boolean isOpponentRobo;
	
	private String player1;
	
	private String player2;
	
	private GameOfThreeSocketEndpoint player1EndPoint;
	
	private GameOfThreeSocketEndpoint player2EndPoint;
	
	private Integer gamePoint;
	
	private String currentPlayer;
	
	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isOpponentRobo() {
		return isOpponentRobo;
	}

	public void setOpponentRobo(boolean isOpponentRobo) {
		this.isOpponentRobo = isOpponentRobo;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public GameOfThreeSocketEndpoint getPlayer1EndPoint() {
		return player1EndPoint;
	}

	public void setPlayer1EndPoint(GameOfThreeSocketEndpoint player1EndPoint) {
		this.player1EndPoint = player1EndPoint;
	}

	public GameOfThreeSocketEndpoint getPlayer2EndPoint() {
		return player2EndPoint;
	}

	public void setPlayer2EndPoint(GameOfThreeSocketEndpoint player2EndPoint) {
		this.player2EndPoint = player2EndPoint;
	}

	public Integer getGamePoint() {
		return gamePoint;
	}

	public void setGamePoint(Integer gamePoint) {
		this.gamePoint = gamePoint;
	}
	
	
}
