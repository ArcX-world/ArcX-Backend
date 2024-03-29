package avatar.util.system;

import java.util.Collection;
import java.util.Random;

public class RandomUtil {

	private static final ThreadLocal<Random> tlRand = new ThreadLocal<Random>() {

		@Override
		protected Random initialValue() {
			return new Random();
		}

	};

	private RandomUtil() {
	}

	public static Random getRandom() {
		return tlRand.get();
	}

	public static boolean bRand() {
		return getRandom().nextBoolean();
	}

	public static int iRand() {
		return getRandom().nextInt();
	}

	/**
	 * @return [0, n)
	 */
	public static int iRand(int n) {
		return getRandom().nextInt(n);
	}

	/**
	 * @return [x, y]
	 */
	public static int iRand(int x, int y) {
		if (x > y) {
			throw new IllegalArgumentException(String.format("x:%s < y:%s ", x, y));
		}
		return getRandom().nextInt(y - x + 1) + x;
	}

	/**
	 * @return [0, 1)
	 */
	public static float fRand() {
		return getRandom().nextFloat();
	}

	/**
	 * @return [0, x)
	 */
	public static float fRand(float x) {
		return fRand() * x;
	}

	/**
	 * @return [x, y)
	 */
	public static float fRand(float x, float y) {
		return x + fRand() * (y - x);
	}

	public static boolean randomPersent(int persent) {
		return getRandom().nextInt(100) < persent;
	}

	public static boolean random(double rate) {
		return Math.random() < rate;
	}

	/**
	 * @param rates
	 * @return 返回随机到的下标 从[0,rates.length-1]
	 */
	public static int randomInRate(float... rates) {
		float randomRate = getRandom().nextFloat();
		float initRate = 0;
		int i = 0;
		while (i < rates.length) {
			initRate += rates[i];
			if (randomRate <= initRate) {
				return i;
			}
			i++;
		}
		return rates.length - 1;
	}

	/**
	 * @param rates
	 * @return 返回随机到的下标 从[0,rates.length-1]
	 */
	public static int randomInRate(Float... rates) {
		float randomRate = getRandom().nextFloat();
		float initRate = 0;
		int i = 0;
		while (i < rates.length) {
			initRate += rates[i];
			if (randomRate <= initRate) {
				return i;
			}
			i++;
		}
		return rates.length - 1;
	}

	/**
	 * @param rates
	 * @return 返回随机到的下标 从[0,rates.size()-1]
	 */
	public static int randomInRate(Collection<Float> rates) {
		Float[] array = new Float[rates.size()];
		rates.toArray(array);
		return randomInRate(array);
	}


	/**
	 * Select an <code>int</code> value between <code>min</code> and <code>max</code> by random. Both <code>min</code>
	 * and <code>max</code> might be selected.
	 *
	 * @param min
	 * @param max
	 * @return
	 */
	public static int avgRandom(int min, int max) {
		if (min > max) {
			int temp = max;
			max = min;
			min = temp;
		}
		int rNum = getRandom().nextInt(max - min + 1);
		return rNum + min;
	}

	/**
	 * Select an <code>int</code> value between <code>min</code> and <code>max</code> by random. Both <code>min</code>
	 * and <code>max</code> might be selected.
	 *
	 * @param min
	 * @param max
	 * @return
	 */
	public static int avgRandom(int min, int max, int step) {
		if (min > max) {
			int temp = max;
			max = min;
			min = temp;
		}

		int length = calStepLength(min, max, step);
		int randIndex = getRandom().nextInt(length);
		int value = min + randIndex * step;
		value = value > max ? max : value;
		return value;
	}

	public static int calStepLength(int startNum, int endNum, int numStep) {
		int range = endNum - startNum;
		int length = (range / numStep) + 1;
		length = range % numStep == 0 ? length : length + 1;
		return length;
	}

	/**
	 * Select an <code>double</code> value between <code>min</code> and <code>max</code> by random. Both
	 * <code>min</code> and <code>max</code> might be selected.
	 *
	 * @param min
	 * @param max
	 * @return
	 */
	public static double avgRandom(double min, double max) {
		if (min > max) {
			double temp = max;
			max = min;
			min = temp;
		}
		double rNum = getRandom().nextDouble() * (max - min);
		return rNum + min;
	}

}
