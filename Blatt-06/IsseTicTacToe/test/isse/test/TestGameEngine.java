package isse.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import isse.model.GameEngine;
import isse.model.PlayStrategy;
import isse.model.Player;
import isse.model.strategies.AlphaBetaStrategy;
import isse.model.strategies.InteractiveStrategy;
import isse.model.strategies.MiniMaxStrategy;
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
	// @Ignore
	public void testNaiveStrategies() {
		PlayStrategy naive = new NaiveStrategy();
		engine.registerStrategy(Player.CROSSES, naive);
		engine.registerStrategy(Player.NOUGHTS, naive);

		// now play
		engine.play();
	}

	@Test
	// @Ignore
	public void testRandomVsNaiveStrategie() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new RandomStrategy();
		oStrategy = new NaiveStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testReflexVsNaiveStrategie() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyHard();
		oStrategy = new NaiveStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsRandomStrategie() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new RandomStrategy();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsReflexStrategieLvl0() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyEasy();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsReflexStrategieLvl1() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyNormal();
		oStrategy = new RandomStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testRandomVsReflexStrategieLvl2() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new RandomStrategy();
		oStrategy = new ReflexStrategyHard();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testReflexVsReflexStrategie() {
		int ANZAHL_SPIELE = 100000;

		xStrategy = new ReflexStrategyEasy();
		oStrategy = new ReflexStrategyEasy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testReflexVsMinMaxStrategie() {
		int ANZAHL_SPIELE = 50;
		xStrategy = new ReflexStrategyHard();
		oStrategy = new MiniMaxStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testMinMaxVsReflexStrategie() {
		int ANZAHL_SPIELE = 50;
		xStrategy = new MiniMaxStrategy();
		oStrategy = new ReflexStrategyHard();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testMiniMaxVsMiniMaxStrategie() {
		int ANZAHL_SPIELE = 50;
		xStrategy = new MiniMaxStrategy();
		oStrategy = new MiniMaxStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testReflexVsAlphaBetaStrategie() {
		int ANZAHL_SPIELE = 10000;
		AlphaBetaStrategy temp = new AlphaBetaStrategy();
		temp.setSuchtiefe(4);
		xStrategy = temp;
		oStrategy = new ReflexStrategyHard();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testAlphaBetaVsAlphaBetaStrategie() {
		int ANZAHL_SPIELE = 10000;
		xStrategy = new AlphaBetaStrategy();
		oStrategy = new AlphaBetaStrategy();

		playNGames(ANZAHL_SPIELE);
		showStatistics();
	}

	@Test
	// @Ignore
	public void testHumanStrategies() {
		engine.surpressOutput = false;
		PlayStrategy interactive = new InteractiveStrategy();
		PlayStrategy temp = new NaiveStrategy();
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
		int winsOfX = (int) winners.stream()
				.filter(p -> Player.CROSSES.equals(p)).count();
		System.out.println("Wins of X (" + xStrategy.getClass().getSimpleName()
				+ "): " + winsOfX);
		int winsOfO = (int) winners.stream()
				.filter(p -> Player.NOUGHTS.equals(p)).count();
		System.out.println("Wins of O (" + oStrategy.getClass().getSimpleName()
				+ "): " + winsOfO);
		System.out.println();
	}

}
