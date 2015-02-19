package com.epam.battleship.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.epam.battlefield.enumerates.HitEnum;
import com.epam.battleship.readerfunctions.ShipDetail;
import com.epam.battleship.readerfunctions.ShipReader;

public class Board {
	private int maxX;
	private int maxY;

	private int[][] board;

	private Map<Integer, Ship> ships = new HashMap<>();
	private Random rng;

	public Board(Integer maxX, Integer maxY) {
		this.maxX = maxX;
		this.maxY = maxY;

		board = new int[maxX][maxY];
		rng = new Random();

		List<ShipDetail> details = new ShipReader().getAllShips();
		for (ShipDetail sd : details) {
			addBatchShip(sd);
		}

	}

	private void addBatchShip(ShipDetail sd) {
		for (int i = 0; i < sd.getCount(); i++) {
			Ship ship = createShipFromDetail(sd);
			addShip(ship);
		}
	}

	private boolean placeablePoints(List<Point> points, int x, int y) {
		boolean placeable = true;
		for (Point point : points) {
			if (xCoordinateBiggerThanBoard(x, point) || yCoordinateBiggerThanBoard(y, point) || pointNotFitOnBoard(x, y, point)) {
				placeable = false;
			}
			
			if(x>1 && board[x-1][y]!=0){placeable = false;}
			if(y>1 && board[x][y-1]!=0){placeable = false;}
			if(x<maxX-1 && board[x+1][y]!=0){placeable = false;}
			if(y<maxY-1 && board[x][y+1]!=0){placeable = false;}
		}
		return placeable;
	}

	private boolean pointNotFitOnBoard(int x, int y, Point point) {
		return board[x + point.x][y + point.y] != 0;
	}

	private boolean yCoordinateBiggerThanBoard(int y, Point point) {
		return y + point.y >= maxY;
	}

	private boolean xCoordinateBiggerThanBoard(int x, Point point) {
		return x + point.x >= maxX;
	}

	private List<Point> getPossiblePositions(List<Point> points) {
		List<Point> possiblePositions = new ArrayList<Point>();
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				if (placeablePoints(points, x, y)) {
					possiblePositions.add(new Point(x, y));
				}
			}
		}
		return possiblePositions;
	}

	private Ship createShipOnRandomPoint(ShipDetail shipDetail) {
		List<Point> coordinates = createNewShipCoordinatesFromDetail(shipDetail);
		List<Point> possiblePositions = getPossiblePositions(coordinates);
		int randomPositionIndex = rng.nextInt(possiblePositions.size());
		Point startPoint = possiblePositions.get(randomPositionIndex);
		createAbolsuteCoordinates(coordinates, startPoint);
		return new Ship(coordinates);
	}

	private void createAbolsuteCoordinates(List<Point> coordinates, Point startPoint) {
		for (Point p : coordinates) {
			p.x += startPoint.x;
			p.y += startPoint.y;
		}
	}

	private List<Point> createNewShipCoordinatesFromDetail(ShipDetail shipDetail) {
		List<Point> coordinates = new ArrayList<>();

		int[][] points = shipDetail.getRelativePoints();
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				if (points[i][j] > 0) {
					coordinates.add(new Point(j, i));
				}
			}
		}

		return coordinates;
	}

	private void addShip(Ship ship) {
		ships.put(ship.id, ship);
		putShipCoordinatesInTheMatrix(ship.getCoordinates(), ship.id);
	}

	private void putShipCoordinatesInTheMatrix(List<Point> coordinates, int id) {
		for (Point p : coordinates) {
			board[p.x][p.y] = id;
		}
	}

	private Ship createShipFromDetail(ShipDetail shipDetail) {
		return createShipOnRandomPoint(shipDetail);
	}

	public void printBoard() {
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				if (board[i][j] > 0) {
					System.out.print(String.format("%2d", board[i][j]));
				} else if (board[i][j] == -1) {
					System.out.print(" x");
				} else {
					System.out.print(" .");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public HitEnum isHit(Point point) {
		HitEnum hit = HitEnum.MISS;
		if (board[point.x][point.y] > 0) {
			int shipId = board[point.x][point.y];
			Ship ship = ships.get(shipId);
			ship.takeDamage();
			board[point.x][point.y] = -1;
			hit = ship.isSunk() ? HitEnum.SUNK : HitEnum.HIT;
			printBoard();
		}
		return hit;
	}

	public int getMaxx() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public boolean isGameOver() {
		for (int i = 0; i < maxX; i++) {
			for (int j = 0; j < maxY; j++) {
				if (board[i][j] > 0) {
					return false;
				}
			}
		}
		return true;
	}
}
