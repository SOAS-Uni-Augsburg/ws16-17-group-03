package isse.model.strategies;

import java.util.HashMap;
import java.util.Map;

import isse.model.PlayStrategy;

/**
 * Register strategies you want to offer here
 * 
 * @author isse-soas
 *
 */
public class StrategyProvider {

	private Map<String, Class<? extends PlayStrategy>> strategies;

	public StrategyProvider() {
		strategies = new HashMap<String, Class<? extends PlayStrategy>>();

		// "Human", "Naive", "Random", "Reflex", "MiniMax", "AlphaBeta"
		strategies.put("Human", InteractiveUIStrategy.class);
		strategies.put("Naive", NaiveStrategy.class);
		strategies.put("Random", RandomStrategy.class);
		strategies.put("Reflex easy", ReflexStrategyEasy.class);
		strategies.put("Reflex normal", ReflexStrategyNormal.class);
		strategies.put("Reflex hard", ReflexStrategyHard.class);
	}

	public String[] provideStrategyKeywords() {
		String[] keysArray = new String[strategies.size()];

		keysArray = strategies.keySet().toArray(keysArray);
		return keysArray;
	}

	public PlayStrategy getStrategy(String key) {
		Class<? extends PlayStrategy> strategyClass = strategies.get(key);
		PlayStrategy strategy = null;
		if (strategyClass != null) {
			try {
				strategy = strategyClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return strategy;
	}

}
