package ningbbs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageOutputStream;


public class FileUtil {

	public static String filterSqlStr(String param) {
		param = param.replaceAll("</.*?>|<.*?>", "");
		return param;
	}

	/**
	 * ����HTML ����������ļ���
	 * 
	 * @param html
	 * @return
	 * @throws IOException
	 */
	public static void saveHtml(String filePath, String html) throws IOException {
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write(html.getBytes());
		fos.flush();
		fos.close();
	}



	public static void saveImage(String imagePath, byte[] bytes) throws IOException {
		FileImageOutputStream imgout = new FileImageOutputStream(new File(imagePath));
		imgout.write(bytes, 0, bytes.length);
		imgout.close();
	}

	public static String readHtml(String path) throws IOException {
		String html = "";
		FileInputStream fis = new FileInputStream(path);
		int length = -1;
		byte[] b = new byte[1024000];
		while ((length = fis.read(b)) != -1) {
			html += new String(b, 0, length);
		}
		fis.close();
		return html;
	}

	public static String[] idStr_To_Arr(String ids) {
		return ids.split(",");
	}

	/**
	 * unicode���루unicode����ת���ģ�
	 *
	 * @param theString
	 * @return
	 */
	public static String unicodeDecode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);

				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/**
	 * �ָ�List
	 * 
	 * @author bianrx
	 * @date 2012.1.13
	 * @param list
	 *            ���ָ��list
	 * @param pageSize
	 *            ÿ��list�Ĵ�С
	 * @return List<<List<T>>
	 */
	public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
		int listSize = list.size(); // list�Ĵ�С
		int page = (listSize + (pageSize - 1)) / pageSize; // ҳ��
		List<List<T>> listArray = new ArrayList<List<T>>(); // ����list����
															// ,��������ָ���list
		for (int i = 0; i < page; i++) { // ���������С����
			List<T> subList = new ArrayList<T>(); // ����ÿһλ����һ���ָ���list
			for (int j = 0; j < listSize; j++) { // �������ָ��list
				int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize; // ��ǰ��¼��ҳ��(�ڼ�ҳ)
				if (pageIndex == (i + 1)) { // ��ǰ��¼��ҳ�����Ҫ�����ҳ��ʱ
					subList.add(list.get(j)); // ����list�е�Ԫ�ص��ָ���list(subList)
				}
				if ((j + 1) == ((j + 1) * pageSize)) { // ������һҳʱ�˳���ǰѭ��
					break;
				}
			}
			listArray.add(subList); // ���ָ���list�����Ӧ�������λ��
		}
		return listArray;
	}
}
