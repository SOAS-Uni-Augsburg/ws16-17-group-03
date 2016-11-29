package isse.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import isse.model.GameEngine;
import isse.model.PlayStrategy;
import isse.model.Player;
import isse.model.strategies.InteractiveStrategy;
import isse.model.strategies.NaiveStrategy;
import isse.model.strategies.RandomStrategy;

public class TestGameEngine {

	private GameEngine engine;
	private PlayStrategy xStrategy;
	private PlayStrategy oStrategy;
	private List<Player> winners;

	@Before
	public void setup() {
		engine = new GameEngine();
		winners = new ArrayList<Player>();
	}

	@Test
	public void testNaiveStrategies() {
		PlayStrategy naive = new NaiveStrategy();
		engine.registerStrategy(Player.CROSSES, naive);
		engine.registerStrategy(Player.NOUGHTS, naive);

		// now play
		engine.play();
	}

	@Test
	public void testRandomVsNaiveStrategie() {
		int ANZAHL_SPIELE = 10000;

		xStrategy = new NaiveStrategy();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	@Ignore
	public void testHumanStrategies() {
		PlayStrategy interactive = new InteractiveStrategy();
		engine.registerStrategy(Player.CROSSES, interactive);
		engine.registerStrategy(Player.NOUGHTS, interactive);

		// now play
		engine.play();
	}

	private void playNGames(int anz) {

		// now play
		for (int i = 0; i < anz; i++) {
			engine = new GameEngine();
			engine.registerStrategy(Player.CROSSES, oStrategy);
			engine.registerStrategy(Player.NOUGHTS, xStrategy);

			winners.add(engine.play());

		}
	}

	private void showStatistics() {
		int draws = (int) winners.stream().filter(p -> p == (null)).count();
		System.out.println("Draws: " + draws);
		int winnsOfX = (int) winners.stream().filter(p -> Player.CROSSES.equals(p)).count();
		System.out.println("Winns of X (" + xStrategy.getClass().getSimpleName() + "): " + winnsOfX);
		int winnsOfO = (int) winners.stream().filter(p -> Player.NOUGHTS.equals(p)).count();
		System.out.println("Winns of O (" + oStrategy.getClass().getSimpleName() + "): " + winnsOfO);
	}

}
