package com.app.gameofthree.playercpu;

import java.io.IOException;

import com.app.gameofthree.model.Message;

public interface AutoUser {

	void start() throws InterruptedException, IOException;

	void onMessage(Message message);

}
