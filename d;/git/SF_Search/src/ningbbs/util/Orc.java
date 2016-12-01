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
 * ORCʶ����
 * 
 * @author ningbbs
 *
 */
public class Orc {
	public String getNums() throws IOException {
		BufferedImage img = ImageIO.read(new File("code.jpg"));
		String color = handelPoints(img);// ��ȡ����ɫ
		String n1 = handelNum(img, color, 5, 13); // ʶ���һλ
		String n2 = handelNum(img, color, 15, 23); // ʶ��ڶ�λ
		String n3 = handelNum(img, color, 25, 33); // ʶ�����λ
		String n4 = handelNum(img, color, 35, 43); // ʶ�����λ
		return n1 + n2 + n3 + n4;
	}

	// ȥ�������߿�,���ж����ֵ���ɫ
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
						// ȥ��,���߿�,����ɫ�����϶���,200���ϵ�RGBֵ,���嶼��200���µ�RGBֵ,100,100,100�Ǳ߿�Ĺ̶���ɫֵ
						put(colors, rgb, ++c);// ��ȥ����,������ɫ��MAP
					}
				}
			}
		}
		c = 0;
		Iterator<Map.Entry<String, Integer>> entries = colors.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, Integer> entry = entries.next();
			if (entry.getValue() > c) {// �ж�map��������ɫ���,���϶�Ϊ��������ɫ
				c = entry.getValue();
				result = entry.getKey();
			}
		}
		return result;
	}

	// ȡ���ؾ���ĳһ�е�����ɫ����(����ɫһ�����������ɫ)
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

	// ����ͼ���������
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

	// ��������תΪ����
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
		case 39:// 6(��Ϊ39����)
			num = "6";
			break;
		case 26:// 7
			num = "7";
			break;
		case 40:// 8
			num = "8";
			break;
		case 93:// 9(��Ϊ39����)
			num = "9";
			break;
		}
		return num;
	}

	// RGBֵת�ַ���
	private String intArrToStr(int[] ia) {
		String tmp = "";
		for (int i : ia) {
			tmp = tmp + "," + i;
		}
		return tmp;
	}

	// ȡָ�����RGB��Ϣ
	private int[] getRGB(BufferedImage image, int x, int y) {
		int[] rgb = new int[3];
		int pixel = image.getRGB(x, y);
		rgb[0] = (pixel & 0xff0000) >> 16;
		rgb[1] = (pixel & 0xff00) >> 8;
		rgb[2] = (pixel & 0xff);
		return rgb;
	}

	// �˷���PUT��ԭֵ�������ۼ�
	private static void put(Map<String, Integer> colors, String key, int num) {
		Integer oldValue = colors.put(key, num);
		if (oldValue != null) {
			colors.put(key, num + oldValue);
		}
	}

	// ����ָ����ָ���е�������Ϣ,ת�����ж����ص���,���ݵ������ؾ��������
	private String handelNum(BufferedImage img, String fontColor, int startWidth, int endWidth) {
		int result = -1;
		int iNum = 0;
		for (int h = 0; h < img.getHeight(); h++) {// ��
			for (int w = startWidth; w < endWidth; w++) {// ��
				String iStr = intArrToStr(getRGB(img, w, h));
				if (iStr.equals(fontColor)) {
					iNum++;
				}
			}
		}
		if (iNum >= 26 && iNum <= 40) {
			result = iNum;
		}
		if (iNum == 39) {// (��Ϊ39����),����6��9�� 6�ĵĵ�һ��Ϊ6����,9�ĵ�һ��Ϊ2����
			if (getPointSize(getImgRect(img), img, fontColor, startWidth) == 2) {
				result = 93;// �������9����֤,�����÷���ֵ=93,������Ϊ6 ����39
			}
		}
		return int_To_Num(result);
	}
}
