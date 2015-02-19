package com.epam.battleship.logic;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;



import com.epam.battleship.commands.AttackCommand;
import com.epam.battleship.commands.Command;
import com.epam.battleship.commands.FireCommand;

public class Intelligence {
	private static final String HIT = "HIT";

	private static final String LOST = "LOST";

	private static final String FIRE = "FIRE";

	private Board board;

	private int boardX;
	private int boardY;

	private List<Point> unusedSpots = new ArrayList<>();

	private Random rng;

	private Point lastHit;
	private boolean enemyLost = false;

	private List<Point> shipPointsFound = new ArrayList<>();
	private boolean lastHitSuccess;

	private int occurranceMatrix[][];

	private int guessCount = 0;

	public Intelligence(Board board) throws IOException {
		this.board = board;
		boardX = board.getMaxx();
		boardY = board.getMaxY();
		rng = new Random();

		for (int x = 0; x < boardX; x++) {
			for (int y = 0; y < boardY; y++) {
				unusedSpots.add(new Point(x, y));
			}
		}
		ShipPossibilitys possibilitymatrix = new ShipPossibilitys();
		occurranceMatrix = possibilitymatrix.getMatrix();

	}

	public Command handleCommand(String command) {
		Command cmd = null;

		if (command.startsWith(FIRE)) {
			cmd = handleFire(command);
			lastHitSuccess = false;
		} else if (command.startsWith(LOST)) {
			enemyLost = true;
		} else if (command.startsWith(HIT)) {
			handleHit(command);
		}
		return cmd;
	}

	private void handleHit(String command) {
		shipPointsFound.add(lastHit);
	}

	private Point getPointFromString(String command) {
		String[] data = command.split(" |,");
		int x = Integer.parseInt(data[1]);
		int y = Integer.parseInt(data[2]);
		return new Point(x, y);
	}

	public boolean didILose() {
		return !enemyLost;
	}

	private Command handleFire(String command) {
		return new FireCommand(board, getPointFromString(command));
	}

	public boolean isGameOver() {
		return enemyLost || board.isGameOver();
	}

	public Point generateNewUniqueRandom() {
		int index = rng.nextInt(unusedSpots.size());
		return unusedSpots.remove(index);
	}

	public int getGuessCount() {
		return guessCount;
	}

	public Command getAttackingCommand() {
		guessCount++;
		return new AttackCommand(getNextBestMove());
	}

	public class AttackPointComparator implements Comparator<Point> {

		@Override
		public int compare(Point arg0, Point arg1) {
			return 0;
		}
		
	}



	private Point getNextBestMove() {

		Point bestPoint = null;
		if (lastHit != null && lastHitSuccess) {
			System.err.println("At least its inside");
			int bestValue = occurranceMatrix[0][0];

			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					if (validCoordinate(lastHit.x + x, lastHit.y + y) && emptyCoordinate(x, y)) {
						if (bestValue > occurranceMatrix[x + 1][y + 1]) {
							bestValue = occurranceMatrix[x + 1][y + 1];
							bestPoint = new Point(lastHit.x + x, lastHit.y + y);
						}
					}
				}
			}
		}
		if (bestPoint == null) {
			bestPoint = generateNewUniqueRandom();
		} else {
		}
		lastHit = bestPoint;
		return bestPoint;
	}

	private boolean emptyCoordinate(int x, int y) {
		return unusedSpots.contains(new Point(x, y));
	}

	private boolean validCoordinate(int x, int y) {
		return x > 0 && x < 30 && y > 0 && y < 30;
	}

}
