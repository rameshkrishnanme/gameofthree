package com.app.gameofthree.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.gameofthree.SpringContext;
import com.app.gameofthree.constants.GameStatus;
import com.app.gameofthree.model.Message;
import com.app.gameofthree.model.Playground;
import com.app.gameofthree.service.IPointsService;

@ServerEndpoint(value = "/game/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class GameOfThreeSocketEndpoint {

	@Autowired
	IPointsService iPointsService = SpringContext.getBean(IPointsService.class);;

	private Session session;
	private static final Set<GameOfThreeSocketEndpoint> gameUserEndpoints = new CopyOnWriteArraySet<>();
	private static Map<String, String> users = new HashMap<>();
	private static Map<String, Playground> userPlayground = new HashMap<>();
	private static final Map<String, GameOfThreeSocketEndpoint> gameUserEndpointMap = new HashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {

		this.session = session;
		users.put(session.getId(), username);

		gameUserEndpoints.add(this);
		gameUserEndpointMap.put(users.get(session.getId()), this);

		Message message = new Message();
		message.setMessage(username + " : Connnected !");
		message.setStatus(GameStatus.CONNECTED.getCode());
		sendMessage(message, this);
	}

	@OnMessage
	public void onMessage(Session session, Message message) throws IOException, EncodeException {
		if (message.getStatus().equals(GameStatus.GAME_STARTED.getCode())) {
			joinAGame(session, message.getMessage());
		} else if (message.getStatus().equals(GameStatus.GAME_PLAYING.getCode())) {
			playingAGame(session, message);
		} else {
			// No agreement
		}
	}

	private void joinAGame(Session session, String opponent) throws IOException, EncodeException {
		String player1 = users.get(session.getId());
		String player2 = opponent;

		String playgroudId = UUID.randomUUID().toString();

		Message newMessage = new Message();
		newMessage.setPlayground(playgroudId);
		newMessage.setMessage(String.format("%s : %s , %s", "JOINED", player1, player2));
		newMessage.setStatus(GameStatus.GAME_STARTED.getCode());

		Integer gamePoint = iPointsService.gamePoint();
		newMessage.setPoint(gamePoint);

		// Preparing the Playground
		Playground playground = new Playground();
		playground.setName(newMessage.getPlayground());
		playground.setPlayer1(player1);
		playground.setPlayer2(player2);
		playground.setPlayer1EndPoint(gameUserEndpointMap.get(player1));
		playground.setPlayer2EndPoint(gameUserEndpointMap.get(player2));
		playground.setCurrentPlayer(player1);
		playground.setGamePoint(gamePoint);
		userPlayground.put(playground.getName(), playground);

		sendMessage(newMessage, playground.getPlayer1EndPoint());
		sendMessage(newMessage, playground.getPlayer2EndPoint());
	}

	private void playingAGame(Session session, Message message) throws IOException, EncodeException {
		Playground playground = userPlayground.get(message.getPlayground());
		String messageUser = users.get(session.getId());

		if (!messageUser.equals(playground.getCurrentPlayer())) {
			Message notATurn = new Message();
			notATurn.setMessage("This is not your Turn");
			notATurn.setPoint(playground.getGamePoint());
			notATurn.setStatus(GameStatus.GAME_WRONGTURN.getCode());
			sendMessage(notATurn, gameUserEndpointMap.get(messageUser));
			return;
		}

		Message newMessage = new Message();
		newMessage.setCalculation(iPointsService.displayFormula(message.getPoint(), playground.getGamePoint()));

		Integer computed = iPointsService.computeNewPoint(message.getPoint(), playground.getGamePoint());
		playground.setGamePoint(computed);

		newMessage.setMessage(String.format("%s : %s", messageUser, message.getPoint()));
		newMessage.setPoint(computed);

		String nextTurnPlayer = playground.getCurrentPlayer().equals(playground.getPlayer1()) ? playground.getPlayer2()
				: playground.getPlayer1();

		if (iPointsService.isWinningNumber(computed)) {

			String currentMsg = newMessage.getMessage();

			newMessage.setStatus(GameStatus.GAME_COMPLETED.getCode());
			newMessage.setMessage(currentMsg + "\n" + "You Win" + " (" + playground.getCurrentPlayer() + ")");
			sendMessage(newMessage, gameUserEndpointMap.get(playground.getCurrentPlayer()));

			newMessage.setMessage(currentMsg + "\n" + "You Lose" + " (" + nextTurnPlayer + ")");
			sendMessage(newMessage, gameUserEndpointMap.get(nextTurnPlayer));

		} else {
			newMessage.setStatus(GameStatus.GAME_PLAYING.getCode());
			sendMessage(newMessage, gameUserEndpointMap.get(nextTurnPlayer));

			newMessage.setStatus(GameStatus.GAME_RESPONSE.getCode());
			sendMessage(newMessage, gameUserEndpointMap.get(playground.getCurrentPlayer()));

			playground.setCurrentPlayer(nextTurnPlayer);
		}
		userPlayground.put(message.getPlayground(), playground);

	}

	@OnClose
	public void onClose(Session session) throws IOException, EncodeException {
		gameUserEndpoints.remove(this);
		Message message = new Message();
		message.setMessage("Disconnected!" + " (" + users.get(session.getId() + ")"));

//        sendMessage(message, gameUserEndpointMap.get(users.get(session.getId())));
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
	}

	private static void sendMessage(Message message, GameOfThreeSocketEndpoint endpoint)
			throws IOException, EncodeException {
		synchronized (endpoint) {
			try {
				endpoint.session.getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException e) {
				e.printStackTrace();
			}
		}
//        gameUserEndpoints.forEach(endpoint -> {
//            synchronized (endpoint) {
//                try {
//                    endpoint.session.getBasicRemote().sendObject(message);
//                } catch (IOException | EncodeException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
	}

	public static Map<String, String> getUsers() {
		return users;
	}

}
