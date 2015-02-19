package com.epam.battleship.logic;

import java.awt.Point;
import java.util.List;

public class Ship {
	public List<Point> coordinates;
	public int id;
	public int damage;
	public static int nextShipId = 0;

	public Ship(List<Point> coordinates) {
		this.coordinates = coordinates;
		this.id = nextShipId++;
		System.out.println("Created ship with id: " + id);
	}

	public List<Point> getCoordinates() {
		return coordinates;
	}

	public int getId() {
		return id;
	}

	public boolean isSunk() {
		return coordinates.size() > damage;
	}

	public int getDamage() {
		return damage;
	}

	public void takeDamage() {
		damage++;

	}

}
