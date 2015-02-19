package com.epam.battleship.readerfunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShipReader {

	public List<ShipDetail> getAllShips() {
		return createShipDetailsFromFile(new File("ships.txt"));
	}

	private List<ShipDetail> createShipDetailsFromFile(File file) {
		List<String> lines = tryToReadFile(file);
		List<ShipDetail> details = createDetailsFromLines(lines);
		return details;
	}

	private List<ShipDetail> createDetailsFromLines(List<String> lines) {
		Iterator<String> iterator = lines.iterator();
		List<ShipDetail> results = new ArrayList<>();
		while (iterator.hasNext()) {
			results.add(createShipDetailFromFiveLine(iterator));
		}
		return results;
	}

	private ShipDetail createShipDetailFromFiveLine(Iterator<String> iterator) {
		String firstLine = iterator.next();
		String secondLine = iterator.next();
		String thirdLine = iterator.next();
		String fourthLine = iterator.next();
		int count = Integer.parseInt(iterator.next());
		return new ShipDetail(count, firstLine, secondLine, thirdLine, fourthLine);
	}

	private List<String> tryToReadFile(File file) {
		List<String> lines = new ArrayList<>();
		try {
			lines = readFile(file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return lines;
	}

	private List<String> readFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			lines.add(s);
		}
		fr.close();
		return lines;
	}
}
