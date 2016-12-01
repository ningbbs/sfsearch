package ningbbs.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * ORC识别类
 * 
 * @author ningbbs
 *
 */
public class Orc {
	public String getNums() throws IOException {
		BufferedImage img = ImageIO.read(new File("code.jpg"));
		String color = handelPoints(img);// 获取主颜色
		String n1 = handelNum(img, color, 5, 13); // 识别第一位
		String n2 = handelNum(img, color, 15, 23); // 识别第二位
		String n3 = handelNum(img, color, 25, 33); // 识别第三位
		String n4 = handelNum(img, color, 35, 43); // 识别第四位
		return n1 + n2 + n3 + n4;
	}

	// 去背景及边框,并判断数字的颜色
	private String handelPoints(BufferedImage img) throws IOException {
		String result = "";
		Map<String, Integer> colors = new HashMap<>();
		int c = 0;
		for (int h = 0; h < img.getHeight(); h++) {
			for (int w = 0; w < img.getWidth(); w++) {
				String rgb = intArrToStr(getRGB(img, w, h));
				Pattern p = Pattern.compile(",2\\d\\d");
				if (!rgb.equals(",100,100,100")) {
					Matcher m = p.matcher(rgb);
					if (!m.find()) {
						// 去背,及边框,背景色基本上都是,200以上的RGB值,字体都是200以下的RGB值,100,100,100是边框的固定颜色值
						put(colors, rgb, ++c);// 除去背景,增加颜色到MAP
					}
				}
			}
		}
		c = 0;
		Iterator<Map.Entry<String, Integer>> entries = colors.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, Integer> entry = entries.next();
			if (entry.getValue() > c) {// 判断map里哪种颜色最多,即认定为字体主颜色
				c = entry.getValue();
				result = entry.getKey();
			}
		}
		return result;
	}

	// 取像素矩阵某一列的主颜色数量(主颜色一般就是字体颜色)
	private int getPointSize(String[][] imgRect, BufferedImage img, String fontColor, int columNum) {
		String[] colums = imgRect[columNum];
		int size = 0;
		for (String str : colums) {
			if (str.equals(fontColor)) {
				size++;
			}
		}
		return size;
	}

	// 返回图像总体矩阵
	private String[][] getImgRect(BufferedImage img) {
		String[][] imgRect = new String[img.getWidth()][img.getHeight()];
		for (int h = 0; h < img.getHeight(); h++) {
			for (int w = 0; w < img.getWidth(); w++) {
				String iStr = intArrToStr(getRGB(img, w, h));
				imgRect[w][h] = iStr;
			}
		}
		return imgRect;
	}

	// 把像素数转为数字
	private String int_To_Num(int numCode) {
		String num = "";
		switch (numCode) {
		case 36:// 0
			num = "0";
			break;
		case 27:// 1
			num = "1";
			break;
		case 32:// 2
			num = "2";
			break;
		case 31:// 3
			num = "3";
			break;
		case 35:// 4
			num = "4";
			break;
		case 37:// 5
			num = "5";
			break;
		case 39:// 6(都为39像素)
			num = "6";
			break;
		case 26:// 7
			num = "7";
			break;
		case 40:// 8
			num = "8";
			break;
		case 93:// 9(都为39像素)
			num = "9";
			break;
		}
		return num;
	}

	// RGB值转字符串
	private String intArrToStr(int[] ia) {
		String tmp = "";
		for (int i : ia) {
			tmp = tmp + "," + i;
		}
		return tmp;
	}

	// 取指定点的RGB信息
	private int[] getRGB(BufferedImage image, int x, int y) {
		int[] rgb = new int[3];
		int pixel = image.getRGB(x, y);
		rgb[0] = (pixel & 0xff0000) >> 16;
		rgb[1] = (pixel & 0xff00) >> 8;
		rgb[2] = (pixel & 0xff);
		return rgb;
	}

	// 此方法PUT在原值基础上累加
	private static void put(Map<String, Integer> colors, String key, int num) {
		Integer oldValue = colors.put(key, num);
		if (oldValue != null) {
			colors.put(key, num + oldValue);
		}
	}

	// 处理指定行指定列的数字信息,转换并判断像素点数,根据点数返回具体的数字
	private String handelNum(BufferedImage img, String fontColor, int startWidth, int endWidth) {
		int result = -1;
		int iNum = 0;
		for (int h = 0; h < img.getHeight(); h++) {// 行
			for (int w = startWidth; w < endWidth; w++) {// 列
				String iStr = intArrToStr(getRGB(img, w, h));
				if (iStr.equals(fontColor)) {
					iNum++;
				}
			}
		}
		if (iNum >= 26 && iNum <= 40) {
			result = iNum;
		}
		if (iNum == 39) {// (都为39像素),处理6和9的 6的的第一列为6像素,9的第一列为2像素
			if (getPointSize(getImgRect(img), img, fontColor, startWidth) == 2) {
				result = 93;// 如果符合9的特证,则设置返回值=93,否则视为6 返回39
			}
		}
		return int_To_Num(result);
	}
}
