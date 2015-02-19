package com.epam.battleship.commands;

import java.awt.Point;

public class AttackCommand implements Command {

	private static final String FIRE_FORMAT = "FIRE %d,%d";
	private int x;
	private int y;

	public AttackCommand(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	@Override
	public String getResult() {
		return String.format(FIRE_FORMAT, x, y);
	}
}
