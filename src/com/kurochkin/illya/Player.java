package com.kurochkin.illya;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player {

	private static final Random random = new Random();

	public int[] values = new int[5];
	{
		for (int i = 0; i < values.length; i++) {
			values[i] = random.nextInt(6) + 1;
		}
	}

	public final String name;

	private Combination currentCombination;

	public Player(String name) {
		this.name = name;
		currentCombination = getCombination().getKey();
	}

	public void updateValues() {
		values = Stream.generate(() -> random.nextInt(6) + 1).limit(5).mapToInt(Integer::new).toArray();
	}

	public static Map.Entry<Player, List<String>> getWinner(Player player1, Player player2) {
		Map.Entry<Combination, Integer> player1Combination = player1.getCombination();
		Map.Entry<Combination, Integer> player2Combination = player2.getCombination();

		Player winner;
		if (player1Combination.getKey().getPoints() != player2Combination.getKey().getPoints()) {
			winner = player1Combination.getKey().getPoints() - player2Combination.getKey().getPoints() > 0 ? player1
					: player2;
		} else if (player1Combination.getValue() != player2Combination.getValue()) {
			winner = player1Combination.getValue() - player2Combination.getValue() > 0 ? player1 : player2;
		} else {
			player1.values = Stream.generate(() -> random.nextInt(6) + 1).limit(5).mapToInt(Integer::new).toArray();
			winner = getWinner(player1, player2).getKey();
		}

		return new AbstractMap.SimpleEntry<>(winner,
				Arrays.asList(player1Combination.getKey().toString, player2Combination.getKey().toString));
	}

	public Map.Entry<Combination, Integer> getCombination() {
		Map.Entry<Combination, Integer> combination;
		// five of a kind
		if (values[0] == values[1] && values[1] == values[2] && values[2] == values[3] && values[3] == values[4]) {
			combination = new AbstractMap.SimpleEntry<>(Combination.FIVE_OF_A_KIND, values[0] * 5);

			// four of a kind
		} else if (Arrays.stream(values).mapToObj(Integer::new)
				.collect(Collectors.groupingBy(a -> a, Collectors.counting())).containsValue((long) 4)) {
			combination = new AbstractMap.SimpleEntry<>(Combination.FOUR_OF_A_KIND,
					Arrays.stream(values).sorted().toArray()[2] * 4);

			// full house
		} else if (Arrays.stream(values).mapToObj(Integer::new).collect(Collectors.groupingBy(a -> a)).size() == 2) {
			combination = new AbstractMap.SimpleEntry<>(Combination.FULL_HOUSE,
					Arrays.stream(values).sorted().toArray()[2] * 3);

			// six high straight
		} else if (values[0] == 2 && values[1] == 3 && values[2] == 4 && values[3] == 5 && values[4] == 6) {
			combination = new AbstractMap.SimpleEntry<>(Combination.SIX_HIGH_STRAIGHT, 2 + 3 + 4 + 5 + 6);

			// five high straight
		} else if (values[0] == 1 && values[1] == 2 && values[2] == 3 && values[3] == 4 && values[4] == 5) {
			combination = new AbstractMap.SimpleEntry<>(Combination.FIVE_HIGH_STRAIGHT, 1 + 2 + 3 + 4 + 5);

			// three of a kind
		} else if (Arrays.stream(values).mapToObj(Integer::new)
				.collect(Collectors.groupingBy(a -> a, Collectors.counting())).containsValue((long) 3)) {
			combination = new AbstractMap.SimpleEntry<>(Combination.THREE_OF_A_KIND,
					Arrays.stream(values).sorted().toArray()[2] * 3);

			// two pairs
		} else if (Arrays.stream(values).mapToObj(Integer::new).collect(Collectors.groupingBy((Integer a) -> (int) a))
				.size() == 3) {
			combination = new AbstractMap.SimpleEntry<>(Combination.TWO_PAIRS,
					Arrays.stream(values).mapToObj(Integer::new)
							.collect(Collectors.groupingBy((Integer a) -> (int) a, Collectors.counting())).entrySet()
							.stream().filter(a -> a.getValue() == 2).mapToInt(a -> a.getKey() * 2).sum());

			// one pair
		} else if (Arrays.stream(values).mapToObj(Integer::new).collect(Collectors.groupingBy((Integer a) -> (int) a))
				.size() == 4) {
			combination = new AbstractMap.SimpleEntry<>(Combination.ONE_PAIR,
					Arrays.stream(values).mapToObj(Integer::new)
							.collect(Collectors.groupingBy((Integer a) -> (int) a, Collectors.counting())).entrySet()
							.stream().filter(a -> a.getValue() == 2).mapToInt(a -> a.getKey() * 2).findAny()
							.getAsInt());

			// nothing
		} else {
			combination = new AbstractMap.SimpleEntry<>(Combination.NOTHING, Arrays.stream(values).sum());
		}
		currentCombination = combination.getKey();
		return combination;
	}

	public int[] getLightIndexes() {
		System.out.println(currentCombination == null);
		switch (getCombination().getKey()) {
		case FULL_HOUSE:
		case FIVE_HIGH_STRAIGHT:
		case SIX_HIGH_STRAIGHT:
		case FIVE_OF_A_KIND:
			return new int[] { 0, 1, 2, 3, 4 };
		case FOUR_OF_A_KIND:
			return Stream.iterate(0, a -> a + 1).limit(values.length)
					.map(a -> new AbstractMap.SimpleEntry<>(a, values[a]))
					.collect(Collectors.groupingBy(a -> a.getValue())).entrySet().stream()
					.filter(a -> a.getValue().size() == 4).findAny().get().getValue().stream()
					.mapToInt(SimpleEntry::getKey).toArray();
		case THREE_OF_A_KIND:
			return Stream.iterate(0, a -> a + 1).limit(values.length)
					.map(a -> new AbstractMap.SimpleEntry<>(a, values[a]))
					.collect(Collectors.groupingBy(a -> a.getValue())).entrySet().stream()
					.filter(a -> a.getValue().size() == 3).findAny().get().getValue().stream()
					.mapToInt(SimpleEntry::getKey).toArray();
		case TWO_PAIRS:
		case ONE_PAIR:
			return Stream.iterate(0, a -> a + 1).limit(values.length)
					.map(a -> new AbstractMap.SimpleEntry<>(a, values[a]))
					.collect(Collectors.groupingBy(a -> a.getValue())).entrySet().stream()
					.filter(a -> a.getValue().size() == 2)
					.flatMapToInt(a -> a.getValue().stream().mapToInt(SimpleEntry::getKey)).toArray();
		default:
			return new int[] {};
		}
	}

	@Override
	public String toString() {
		return "Player : " + Arrays.toString(values);
	}

	public String getName() {
		return name;
	}
}