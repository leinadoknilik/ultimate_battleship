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

public class TorpedoClient implements Runnable {
	private int port;
	private String hostName;
	private Socket socket;
	private OutputStream out;

	private Board board;
	private BufferedReader in;
	private ArtificalIntelligence ai;

	public TorpedoClient(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}

	boolean isStopped = false;

	public void initStreams() throws IOException {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = socket.getOutputStream();

	}

	public void returnResponse(Command data) throws IOException {
		String result = data.getResult();
		System.out.println("[client] wrote: " + result);
		out.write((result + "\n").getBytes());
		out.flush();
	}

	@Override
	public void run() {
		try {
			socket = new Socket(hostName, port);
			initStreams();
			readBoardParametersAndInit();

			attackEnemy();

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

			// returnResponse(new GameoverCommand());
			System.out.println(ai.getGuessCount());
			in.close();
			out.close();
			isStopped = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readBoardParametersAndInit() throws IOException {
		String tableSize = in.readLine();
		String[] data = tableSize.split(" |,");
		int tableX = Integer.parseInt(data[1]);
		int tableY = Integer.parseInt(data[2]);

		board = new Board(tableX, tableY);
		ai = new ArtificalIntelligence(board, "client");
	}

	private void attackEnemy() throws IOException {
		Command command = ai.getAttackingCommand();
		returnResponse(command);
	}

}
