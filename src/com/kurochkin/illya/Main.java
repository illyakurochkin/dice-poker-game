package com.kurochkin.illya;

import acm.program.ConsoleProgram;

public class Main {
	private static String firstName;
	private static String secondName;
	
	@SuppressWarnings("serial")
	public static void main(String[] args) {
		new ConsoleProgram() {
			public void run() {
				setSize(200,200);
				firstName = readLine("enter first name : ").trim();
				secondName = readLine("enter second name : ").trim();
				
			}
		}.start();
		Game game = new Game(firstName, secondName);
		game.startGame();
	}
}
