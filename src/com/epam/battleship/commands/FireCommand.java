package com.epam.battleship.commands;

import java.awt.Point;

import com.epam.battlefield.enumerates.HitEnum;
import com.epam.battleship.logic.Board;

public class FireCommand implements Command {

	private Board board;
	private Point point;

	public FireCommand(Board board, Point point) {
		this.board = board;
		this.point = point;
	}

	@Override
	public String getResult() {
		HitEnum hit = board.isHit(point);
		return hit.toString();
	}

}
