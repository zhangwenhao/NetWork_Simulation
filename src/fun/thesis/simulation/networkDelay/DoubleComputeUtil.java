package fun.thesis.simulation.networkDelay;

import java.math.BigDecimal;

public class DoubleComputeUtil {

	// divide
	public static double div(double d1, double d2) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2,5,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
