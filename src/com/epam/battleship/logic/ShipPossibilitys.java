package com.epam.battleship.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ShipPossibilitys {
	private int[][] shipMatrix;
	private int[][] possibilityMatrix = new int[3][3];

	private void generateMatrix(int[][] matrix1, int x, int y) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (matrix1[x + i][y + j] == 1) {
					possibilityMatrix[i + 1][j + 1]++;
				}
			}
		}
	}

	public void printMatrix() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(String.format("%3d", possibilityMatrix[i][j]));
			}
			System.out.println();
		}
	}

	public ShipPossibilitys() throws IOException {
		String fileName = "ships.txt";
		BufferedReader br = new BufferedReader(new FileReader(fileName));

		int lineCount = 0;

		possibilityMatrix = new int[3][3];

		shipMatrix = new int[4][4];
		while (br.ready()) {
			String line = br.readLine();
			if (lineCount == 5) {
				lineCount = 0;

				for (int i = 1; i <= 2; i++) {
					for (int j = 1; j <= 2; j++) {
						generateMatrix(shipMatrix, i, j);

					}
				}
				shipMatrix = new int[4][4];

			}
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == 'x') {
					shipMatrix[lineCount][i] = 1;
				}
			}
			System.out.println(lineCount + " " + line);
			lineCount++;
		}
		br.close();
	}

	public int[][] getMatrix() {
		return possibilityMatrix;
	}
}
