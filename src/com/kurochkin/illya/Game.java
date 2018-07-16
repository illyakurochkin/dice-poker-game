package com.kurochkin.illya;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

@SuppressWarnings("serial")
public class Game extends GraphicsProgram {

	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 580;
	public static final String TITLE = "Witcher dice poker";

	private static final String NAME_FONT = "Segoe UI-50";// Yu Gothic Medium
	private static final String INFO_CHANGE_FONT = "Segoe UI-70";
	private static final String MORE_INFO_CHANGE_FONT = "Segoe UI-40";

	private final GImage background = new GImage("images/hello.jpg");
	private Player player1;
	private Player player2;

	private Dice[] diceLine1;
	private Dice[] diceLine2;

	private final ConfirmButton button1 = new ConfirmButton(110, 64, 55);
	private final ConfirmButton button2 = new ConfirmButton(110, 364, 55);

	private GLabel nameLabel1;
	private GLabel nameLabel2;

	private MouseListener confirmButtonListener = new MouseAdapter() {

		@Override
		public void mouseEntered(MouseEvent event) {
			ConfirmButton button = (ConfirmButton) event.getSource();
			button.move(-1, -1);
			button.setSize(button.getSize().getWidth() + 2, button.getSize().getHeight()+ 2);
		}

		@Override
		public void mouseExited(MouseEvent event) {
			ConfirmButton button = (ConfirmButton) event.getSource();
			button.move(1, 1);
			button.setSize(button.getSize().getWidth() - 2, button.getSize().getHeight() - 2);
		}

		@Override
		public void mousePressed(MouseEvent event) {
			ConfirmButton button = (ConfirmButton) event.getSource();

			Arrays.stream(button == button1 ? diceLine1 : diceLine2).forEach(a -> {
				if (button.isSelected()) {
					a.addMouseListener(a.mouseListener);
				} else {
					a.removeMouseListener(a.mouseListener);
				}
			});
			button.select();

			if (button1.isSelected() && button2.isSelected()) {

				pause(500, () -> {
					button1.select();
					button2.select();
					remove(button1);
					remove(button2);
					thirdPart();
				});
			}
		}
	};

	public static void main(String[] args) {
		new Game("1", "2").startGame();
	}

	@Override
	public void init() {
		setSize(WINDOW_WIDTH + 17, WINDOW_HEIGHT + 63);
		setTitle(TITLE);
		background.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		add(background);
		GRect shadow = new GRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		shadow.setFilled(true);
		shadow.setFillColor(new Color(0, 0, 0, 150));
		add(shadow);
	}

	public Game(String firstPlayerName, String secondPlayerName) {
		player1 = new Player(firstPlayerName);
		player2 = new Player(secondPlayerName);
	}

	public void startGame() {
		start();
		firstPart();
	}

	private void firstPart() {
		diceLine1 = getDiceLine(player1);
		diceLine2 = getDiceLine(player2);

		updateNameLabels();

		drawDiceLine(diceLine1, 140);
		drawDiceLine(diceLine2, 440);

		pause(4000, () -> addInfoAboutSelecting());
	}

	public void pause(long l, Runnable run) {
		new Thread(() -> {
			try {
				Thread.sleep(l);
				run.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void secondPart() {
		Arrays.stream(diceLine1).forEach(a -> a.addMouseListener(a.mouseListener));
		Arrays.stream(diceLine2).forEach(a -> a.addMouseListener(a.mouseListener));

		add(button1);
		add(button2);

		button1.addMouseListener(confirmButtonListener);
		button2.addMouseListener(confirmButtonListener);
	}

	private void thirdPart() {
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			if (diceLine1[i].isSelected()) {
				player1.values[i] = random.nextInt(6) + 1;
			}
			if (diceLine2[i].isSelected()) {
				player2.values[i] = random.nextInt(6) + 1;
			}
		}
		updateNameLabels();
		diceLine1 = getDiceLine(player1);
		diceLine2 = getDiceLine(player2);

		drawDiceLine(diceLine1, 140);
		drawDiceLine(diceLine2, 440);

		pause(3000, () -> drawWinnerInfo(Player.getWinner(player1, player2).getKey().name));
	}

	private void drawWinnerInfo(String winnerName) {
		GRect rect = new GRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		rect.setFillColor(new Color(0, 0, 0, 240));
		rect.setFilled(true);

		GLabel infoLabel = new GLabel("Congratulations");
		infoLabel.setFont(INFO_CHANGE_FONT);
		infoLabel.setLocation((WINDOW_WIDTH - infoLabel.getWidth()) / 2, WINDOW_HEIGHT / 2);
		infoLabel.setColor(new Color(255, 255, 255, 150));

		GLabel moreInfoLabel = new GLabel(winnerName + " is winner");
		moreInfoLabel.setFont(MORE_INFO_CHANGE_FONT);
		moreInfoLabel.setLocation((WINDOW_WIDTH - moreInfoLabel.getWidth()) / 2,
				infoLabel.getY() + infoLabel.getHeight() * 1.25);
		moreInfoLabel.setColor(new Color(255, 255, 255, 150));

		add(rect);
		add(infoLabel);
		add(moreInfoLabel);

		rect.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				rect.removeMouseListener(this);
				remove(rect);
				remove(infoLabel);
				remove(moreInfoLabel);
				pause(500, () -> {
					removeAll();
					init();
					button1.addMouseListener(confirmButtonListener);
					button2.addMouseListener(confirmButtonListener);
					player1.updateValues();
					player2.updateValues();
					diceLine1 = getDiceLine(player1);
					diceLine2 = getDiceLine(player2);
					firstPart();
				});
			}
		});
	}

	private void updateNameLabels() {
		if (nameLabel1 != null && nameLabel2 != null) {
			remove(nameLabel1);
			remove(nameLabel2);
		}

		nameLabel1 = new GLabel(player1.name + " ( " + player1.getCombination().getKey().toString + " )") {
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				super.paint(g2d);
			}
		};

		nameLabel1.setFont(NAME_FONT);
		nameLabel1.setLocation(200, 110);
		add(nameLabel1);

		nameLabel2 = new GLabel(player2.name + " ( " + player2.getCombination().getKey().toString + " )");
		nameLabel2.setFont(NAME_FONT);
		nameLabel2.setLocation(200, 410);
		add(nameLabel2);
	}

	private void addInfoAboutSelecting() {
		GRect rect = new GRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		rect.setFillColor(new Color(0, 0, 0, 240));
		rect.setFilled(true);

		GLabel infoLabel = new GLabel("Select dices to change");
		infoLabel.setFont(INFO_CHANGE_FONT);
		infoLabel.setLocation((WINDOW_WIDTH - infoLabel.getWidth()) / 2, WINDOW_HEIGHT / 2);
		infoLabel.setColor(new Color(255, 255, 255, 150));

		GLabel moreInfoLabel = new GLabel("Press 'ok' symbol to configm");
		moreInfoLabel.setFont(MORE_INFO_CHANGE_FONT);
		moreInfoLabel.setLocation((WINDOW_WIDTH - moreInfoLabel.getWidth()) / 2,
				infoLabel.getY() + infoLabel.getHeight() * 1.25);
		moreInfoLabel.setColor(new Color(255, 255, 255, 150));

		add(rect);
		add(infoLabel);
		add(moreInfoLabel);

		rect.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				remove(rect);
				remove(infoLabel);
				remove(moreInfoLabel);
				rect.removeMouseListener(this);
				secondPart();
			}
		});
	}

	private static Dice[] getDiceLine(Player player) {
		return Arrays.stream(player.values).mapToObj(Dice::new).toArray(Dice[]::new);
	}

	private void drawDiceLine(Dice[] diceLine, int y) {
		Set<Integer> lightIndexes = Arrays
				.stream(diceLine == diceLine1 ? player1.getLightIndexes() : player2.getLightIndexes())
				.mapToObj(Integer::new).collect(Collectors.toSet());
		int interval = (WINDOW_WIDTH - 5 * Dice.DICE_SIZE) / 6;
		int x = interval;
		for (int i = 0; i < 5; i++) {
			if (lightIndexes.contains(i)) {
				diceLine[i].setGreen();
			}
			diceLine[i].setLocation(x, y);
			x += Dice.DICE_SIZE + interval;
			add(diceLine[i]);
		}
	}
}