package com.kurochkin.illya;

public enum Combination {
	NOTHING("nothing"),
	ONE_PAIR("one pair"), 
	TWO_PAIRS("two pair"), 
	THREE_OF_A_KIND("three of a kind"), 
	FIVE_HIGH_STRAIGHT("five high straight"), 
	SIX_HIGH_STRAIGHT("six high straight"),
	FULL_HOUSE("full house"),
	FOUR_OF_A_KIND("four of a kind"), 
	FIVE_OF_A_KIND("five of a kind");

	public final String toString;

	private Combination(String toString) {
		this.toString = toString;
	}

	public int getPoints() {
		return ordinal();
	}
}
