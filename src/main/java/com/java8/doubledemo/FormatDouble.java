package com.java8.doubledemo;

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;

/**
 * double保留两位小数
 */
public class FormatDouble {
	private Logger log = LoggerFactory.getLogger(FormatDouble.class);

	/**
	 * 您还可以使用String的静态方法format（）打印双精度至2个小数位。
	 */
	@Test
	public void stringFromatformatDouble() {
		double d = 2.456534;
		String format = String.format("%.2f", d);
		System.out.println("Double upto 2 decimal places: " + format);
		log.info(() -> String.format("%.2f", 52.1));
	}

	/**
	 * 您可以使用java.util.Formatter的format（）方法将双精度格式设置为两位小数。
	 */
	@Test
	public void formatDouble01() {
		double d = 2555456564556.456534;

		Formatter formatter = new Formatter();
		formatter.format("%.2f", d);

		System.out.println("Double upto 2 decimal places: " + formatter.toString()); // 2555456564556.46

		log.info(() -> formatter.toString());
	}

	/**
	 * 不推荐使用，数字太大的话，就自己转为科学计数法了
	 * 您可以将double转换为BigDecimal，然后使用BigDecimal的setScale（）方法将double格式设置为2个小数位。您可以使用RoundingMode指定舍入行为。
	 */
	@Test
	public void bigDecimalFormatDouble() {
		double d = 465465.456534;
		BigDecimal bd = new BigDecimal(d).setScale(2, RoundingMode.HALF_DOWN);
		log.info(() -> "保留两位小数: " + bd.doubleValue());

		BigDecimal bdDown = new BigDecimal(d).setScale(2, RoundingMode.DOWN);
		log.info(() -> "保留两位小数 - 向下取整 - RoundingMode.DOWN: " + bdDown.doubleValue());

		BigDecimal bdUp = new BigDecimal(d).setScale(2, RoundingMode.UP);
		log.info(() -> "保留两位小数 - 向上取整 - RoundingMode.UP: " + bdUp.doubleValue());
	}

	/**
	 * 推荐这种
	 * 通过提供格式化模式可以使用DecimalFormat来格式化双精度到2个小数位。 您可以使用RoundingMode指定舍入行为。
	 */
	@Test
	public void decimalFormatDouble() {
		DecimalFormat df = new DecimalFormat("#.##");

		double d = 14567893212.456534;
		System.out.println("Double upto 2 decimal places: " + df.format(d)); // 14567893212.46

		// You can use RoundingMode to round double Up or Down
		df.setRoundingMode(RoundingMode.DOWN);
		System.out.println("Double upto 2 decimal places - RoundingMode.DOWN: " + df.format(d)); // 14567893212.45

		df.setRoundingMode(RoundingMode.UP);
		System.out.println("Double upto 2 decimal places - RoundingMode.UP: " + df.format(d)); // 14567893212.46
	}

	/**
	 * 有分隔符，如：7,894,561,232.01。推荐使用
	 * 您还可以使用NumberFormat的setMaximumFractionDigits（）对数字进行约束（以小数位表示），并使用其format（）方法将双精度格式设置为两位小数。
	 */
	@Test
	public void numberFormatDouble() {
		double d1 = 7894561232.009;
		double d2 = 7894561232.979;

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);

		System.out.println("Double d1 upto 2 decimal places: " + nf.format(d1)); // 7,894,561,232.01
		System.out.println("Double d2 upto 2 decimal places: " + nf.format(d2)); // 7,894,561,232.98
	}

	/**
	 * 不推荐使用，数字太大的话，就自己转为科学计数法了
	 *
	 * 使用Apache公共库
	 * 您可以使用Precision的round（）方法将双精度格式设置为两位小数。 Precision类属于Apache common的common-math3库。
	 *
	 *     <dependency>
	 *     <groupId>org.apache.commons</groupId>
	 *     <artifactId>commons-math3</artifactId>
	 *     <version>3.6.1</version>
	 * 	   </dependency>
	 */
	@Test
	public void apacheCommonPrecisionMain() {
		Double d= 78946.456534;
		System.out.println("Double upto 2 decimal places: " + Precision.round(d,2)); // 78946.46
	}

}
