package com.epam.torpedo.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.epam.torpedo.ArtificalIntelligence;
import com.epam.torpedo.Board;
import com.epam.torpedo.commands.Command;
import com.epam.torpedo.commands.GameoverCommand;

public class TorpedoServerWorker implements Runnable {

	private boolean isStopped = false;
	private Socket socket;
	private BufferedReader in;
	private OutputStream out;
	private ArtificalIntelligence ai;
	private int boardSizeY;
	private int boardSizeX;

	public TorpedoServerWorker(Socket clientSocket, int boardSize1, int boardSize2) {
		this.socket = clientSocket;
		this.boardSizeX = boardSize1;
		this.boardSizeY = boardSize2;

		Board board = new Board(boardSize1, boardSize2);

		try {
			ai = new ArtificalIntelligence(board, "server");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printWelcomeMessage() throws IOException {
		out.write(String.format("INIT %d,%d\n", boardSizeX, boardSizeY).getBytes());
	}

	public void initStreams() throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = socket.getOutputStream();

	}

	public void returnResponse(Command data) throws IOException {
		String result = data.getResult();
		System.out.println("[server] " + Thread.currentThread().getId() + " wrote: " + result);
		out.write((result + "\n").getBytes());
		out.flush();
	}

	@Override
	public void run() {
		System.out.println("A client connected" + Thread.currentThread().getId());

		try {
			initStreams();
			printWelcomeMessage();

			while (!isStopped) {
				Command command = null;
				String line = in.readLine();

				command = ai.handleCommand(line);

				if (command != null) {
					returnResponse(command);
					attackEnemy();
				}
				isStopped = ai.isGameOver();
			}
			if (ai.didILose()) {
				returnResponse(new GameoverCommand());
			}
			System.out.println(ai.getGuessCount());

			closeStreams();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void closeStreams() throws IOException {
		in.close();
		out.close();
		socket.close();
	}

	private void attackEnemy() throws IOException {
		Command command = ai.getAttackingCommand();
		returnResponse(command);
	}

}
