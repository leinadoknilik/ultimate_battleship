package com.epam.battleship.commands;

public class GameoverCommand implements Command {

	private static final String LOST = "LOST";

	@Override
	public String getResult() {
		return LOST;
	}

}
