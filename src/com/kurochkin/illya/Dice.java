package com.kurochkin.illya;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import acm.graphics.GImage;

@SuppressWarnings("serial")
public class Dice extends GImage {

	static final int DICE_SIZE = 100;

	private static final String VALUE_1 = "images/dice1.png";
	private static final String VALUE_1_BLUE = "images/dice1b.png";
	private static final String VALUE_1_GREEN = "images/dice1g.png";

	private static final String VALUE_2 = "images/dice2.png";
	private static final String VALUE_2_BLUE = "images/dice2b.png";
	private static final String VALUE_2_GREEN = "images/dice2g.png";

	private static final String VALUE_3 = "images/dice3.png";
	private static final String VALUE_3_BLUE = "images/dice3b.png";
	private static final String VALUE_3_GREEN = "images/dice3g.png";

	private static final String VALUE_4 = "images/dice4.png";
	private static final String VALUE_4_BLUE = "images/dice4b.png";
	private static final String VALUE_4_GREEN = "images/dice4g.png";

	private static final String VALUE_5 = "images/dice5.png";
	private static final String VALUE_5_BLUE = "images/dice5b.png";
	private static final String VALUE_5_GREEN = "images/dice5g.png";

	private static final String VALUE_6 = "images/dice6.png";
	private static final String VALUE_6_BLUE = "images/dice6b.png";
	private static final String VALUE_6_GREEN = "images/dice6g.png";

	private static String getDefaultImagePath(int value) {
		switch (value) {
		case 1:
			return VALUE_1;
		case 2:
			return VALUE_2;
		case 3:
			return VALUE_3;
		case 4:
			return VALUE_4;
		case 5:
			return VALUE_5;
		case 6:
			return VALUE_6;
		}
		return null;
	}

	private static String getGreenImagePath(int value) {
		switch (value) {
		case 1:
			return VALUE_1_GREEN;
		case 2:
			return VALUE_2_GREEN;
		case 3:
			return VALUE_3_GREEN;
		case 4:
			return VALUE_4_GREEN;
		case 5:
			return VALUE_5_GREEN;
		case 6:
			return VALUE_6_GREEN;
		}
		return null;
	}

	private static String getBlueImagePath(int value) {
		switch (value) {
		case 1:
			return VALUE_1_BLUE;
		case 2:
			return VALUE_2_BLUE;
		case 3:
			return VALUE_3_BLUE;
		case 4:
			return VALUE_4_BLUE;
		case 5:
			return VALUE_5_BLUE;
		case 6:
			return VALUE_6_BLUE;
		}
		return null;
	}

	public MouseListener mouseListener = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent e) {
			setSize(DICE_SIZE + 4, DICE_SIZE + 4);
			move(-2, -2);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setSize(DICE_SIZE - 4, DICE_SIZE - 4);
			move(2, 2);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isSelected) {
				setDefault();
			} else {
				setBlue();
			}
			isSelected = !isSelected;
		}
	};

	private final int value;
	private boolean isSelected = false;
	private boolean wasGreen = false;

	public boolean isSelected() {
		return isSelected;
	}

	public Dice(int value) {
		this(value, 0, 0);
	}

	public Dice(int value, double x, double y) {
		super(getDefaultImagePath(value), x, y);
		this.value = value;
		setSize(DICE_SIZE, DICE_SIZE);
	}

	public void setGreen() {
		wasGreen = true;
		setImage(getGreenImagePath(value));
		setSize(DICE_SIZE, DICE_SIZE);
	}

	public void setBlue() {
		setImage(getBlueImagePath(value));
		setSize(DICE_SIZE, DICE_SIZE);
	}

	public void setDefault() {
		if (wasGreen) {
			setGreen();
		} else {
			setImage(getDefaultImagePath(value));
			setSize(DICE_SIZE, DICE_SIZE);
		}
	}
}