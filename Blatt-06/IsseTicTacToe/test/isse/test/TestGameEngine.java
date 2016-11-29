package isse.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import isse.model.GameEngine;
import isse.model.PlayStrategy;
import isse.model.Player;
import isse.model.strategies.InteractiveStrategy;
import isse.model.strategies.NaiveStrategy;
import isse.model.strategies.RandomStrategy;
import isse.model.strategies.ReflexStrategyEasy;
import isse.model.strategies.ReflexStrategyHard;
import isse.model.strategies.ReflexStrategyNormal;

public class TestGameEngine {

	private GameEngine engine;
	private PlayStrategy xStrategy;
	private PlayStrategy oStrategy;
	private List<Player> winners;

	@Before
	public void setup() {
		setupEngine();
		winners = new ArrayList<Player>();
	}

	private void setupEngine() {
		engine = new GameEngine();
		engine.surpressOutput = true;
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
		int ANZAHL_SPIELE = 100000;

		xStrategy = new NaiveStrategy();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	public void testRandomVsRandomStrategie() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new RandomStrategy();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsRuleBasedStrategieLvl1() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyEasy();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsRuleBasedStrategieLvl2() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyNormal();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsRuleBasedStrategieLvl3() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyHard();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testHumanStrategies() {
		engine.surpressOutput = false;
		PlayStrategy interactive = new InteractiveStrategy();
		PlayStrategy temp = new ReflexStrategyEasy();
		engine.registerStrategy(Player.NOUGHTS, temp);
		engine.registerStrategy(Player.CROSSES, interactive);

		// now play
		engine.play();
	}

	private void playNGames(int anz) {

		// now play
		for (int i = 0; i < anz; i++) {
			setupEngine();
			engine.registerStrategy(Player.CROSSES, xStrategy);
			engine.registerStrategy(Player.NOUGHTS, oStrategy);

			Player winner = engine.play();
			// if (Player.NOUGHTS.equals(winner)) {
			// break;
			// }
			winners.add(winner);

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
