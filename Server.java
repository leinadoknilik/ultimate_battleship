package com.epam.torpedo.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TorpedoServer implements Runnable {

	private ServerSocket listener;
	private int port;

	private boolean isStopped = false;
	private int boardSizeX;
	private int boardSizeY;

	public void stop() {
		isStopped = true;
	}

	public TorpedoServer(int port, int boardSizeX, int boardSizeY) {
		this.boardSizeX = boardSizeX;
		this.boardSizeY = boardSizeY;
		this.port = port;
	}

	public void openSocket() {
		try {
			listener = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Listening on " + port);
	}

	@Override
	public void run() {
		openSocket();
		while (!isStopped) {
			Socket clientSocket = null;
			try {
				clientSocket = listener.accept();
				new Thread(new TorpedoServerWorker(clientSocket, boardSizeX, boardSizeY)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
