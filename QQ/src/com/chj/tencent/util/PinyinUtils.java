package com.chj.tencent.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @����:com.chj.util
 * @����:PinyinUtils
 * @����:�»��
 * @ʱ��:2015-8-20 ����3:30:40
 * 
 * @����:ƴ������
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class PinyinUtils {

	/**
	 * ���ݴ�����ַ���(��������),�õ�ƴ��
	 * 
	 * @���� -> HEIMA
	 * @�� ��*& -> HEIMA
	 * @����f5 -> HEIMA
	 * @param str
	 *            �ַ���
	 * @return
	 */
	public static String getPinyin(String str) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		StringBuffer sb = new StringBuffer();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			// ����ǿո�, ����
			if (Character.isWhitespace(c)) {
				continue;
			} else if (c >= -127 && c < 128) {
				// �϶����Ǻ���
				sb.append(c);
			} else {

				String s = "";
				try {
					// ͨ��char�õ�ƴ������. �� -> dan, shan(������)
					s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					sb.append(s);
				}
			}
		}

		return sb.toString();
	}
}
