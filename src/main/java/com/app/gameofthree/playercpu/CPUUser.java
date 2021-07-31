package com.app.gameofthree.playercpu;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.app.gameofthree.constants.GameStatus;
import com.app.gameofthree.model.Message;

@Component
public class CPUUser implements AutoUser {
	
	
	private static final Logger log = LoggerFactory.getLogger(CPUUser.class);


	private static final String SERVER = "ws://localhost:8080/game/";

	int[] gameOfPoint = { 1, 0, -1 };

	public WebSocketClient wsc;
	
	public String playerName = "CPU";

	public String playground;
	
	public CPUUser() {

	}

	@EventListener(ApplicationReadyEvent.class)
	@Override
	public void start() throws InterruptedException, IOException {
		wsc = new WebSocketClient();
		wsc.callback = this;
		wsc.connect(SERVER+playerName+"/");
		Thread.sleep(1000);
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (GameStatus.GAME_STARTED.getCode() == message.getStatus()) {
				playground = message.getPlayground();
			}
			if (GameStatus.GAME_PLAYING.getCode() == message.getStatus()) {
				Thread.sleep(1000);
				// dumb message
				int randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
				
				message.setPlayground(playground);
				message.setPoint(gameOfPoint[randomNum - 1]);
				wsc.sendMessage(message);
			} else {
				log.debug(message.toString());
			}
			
		} catch (Exception e) {
		}
	}

}