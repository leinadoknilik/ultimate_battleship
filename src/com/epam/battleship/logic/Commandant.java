package com.epam.battleship.logic;

import java.io.IOException;


import java.util.ArrayList;
import java.util.Random;

import com.epam.battleship.readerfunctions.Point;



public class Commandant{

	private boolean isStopped = false;
	private Intelligence ai;
	private int boardSizeY;
	private int boardSizeX;
	private Random rng;

	public Commandant( int boardSize1, int boardSize2) {
		this.boardSizeX = boardSize1;
		this.boardSizeY = boardSize2;

		Board board = new Board(boardSize1, boardSize2);
		

		try {
			ai = new Intelligence(board);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void run() {

		ArrayList<Point> cords=new ArrayList<Point>();
		int shootCount=0;
			while (!isStopped) {
				rng = new Random();
				int xCord = rng.nextInt(boardSizeX);
				int yCord = rng.nextInt(boardSizeY);
				Point used = new Point(xCord, yCord);
				while(!cords.contains(used)){
				String line = "FIRE "+xCord+","+yCord;
				System.out.println("WHAAATCH THIS---> "+line);
				ai.handleCommand(line).getResult();	
				isStopped = ai.isGameOver();
				cords.add(used);
				shootCount++ ;
				}
				
			}
			System.out.println(shootCount);

	}


}
