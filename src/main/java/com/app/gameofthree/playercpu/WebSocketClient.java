package com.app.gameofthree.playercpu;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.gameofthree.model.Message;
import com.app.gameofthree.websocket.MessageDecoder;
import com.app.gameofthree.websocket.MessageEncoder;

@ClientEndpoint(decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketClient {
	
	
	private static final Logger log = LoggerFactory.getLogger(WebSocketClient.class);

	protected WebSocketContainer container;
	protected Session userSession = null;

	public AutoUser callback;

	public WebSocketClient() {
		container = ContainerProvider.getWebSocketContainer();
	}

	public void connect(String sServer) {
		try {
			userSession = container.connectToServer(this, new URI(sServer));
		} catch (DeploymentException | URISyntaxException | IOException e) {
			e.printStackTrace();
		}

	}

	public void sendMessage(Message sMsg) throws IOException {
		try {
			userSession.getBasicRemote().sendObject(sMsg);
		} catch (IOException e) {
		} catch (EncodeException e) {
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		log.debug("CPU Connected");
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
	}

	@OnMessage
	public void onMessage(Session session, Message msg) {
		callback.onMessage(msg);
		log.debug(msg.toString());
	}

	public void disconnect() throws IOException {
		userSession.close();
	}
}