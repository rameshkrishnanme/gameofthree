package com.app.gameofthree.constants;

public enum GameStatus {

	GAME_STARTED(100),
	GAME_INTERRUPTED(200),
	GAME_PLAYING(300),
	GAME_RESPONSE(700),
	GAME_COMPLETED(400), 
	GAME_WRONGTURN(500),
	GAME_WRONGINPUT(600), CONNECTED(000);

	private int code;

	GameStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	
	
}
