package com.kurochkin.illya;

import acm.graphics.GImage;

@SuppressWarnings("serial")
public class ConfirmButton extends GImage {

	private static final String DEFAULT_IMAGE_PATH = "ok.png";
	private static final String GREEN_IMAGE_PATH = "okg.png";
	private boolean selected = false;

	public static final int SIZE = 55;

	public ConfirmButton(double x, double y, double width) {
		super(DEFAULT_IMAGE_PATH, x, y);
		setSize(width, width);
	}

	public boolean isSelected() {
		return selected;
	}

	public void select() {
		setImage(!selected ? GREEN_IMAGE_PATH : DEFAULT_IMAGE_PATH);
		setSize(SIZE, SIZE);
		selected = !selected;
	}
}
