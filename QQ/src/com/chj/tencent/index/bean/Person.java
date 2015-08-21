package com.chj.tencent.index.bean;

import com.chj.tencent.util.PinyinUtils;

/**
 * 
 * @����:com.chj.tencent.index.bean
 * @����:Person
 * @����:�»��
 * @ʱ��:2015-8-20 ����10:53:35
 * 
 * @����:һ��java��bean
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class Person implements Comparable<Person> {

	private String name;
	private String pinyin;

	public Person(String name) {
		super();
		this.name = name;
		this.pinyin = PinyinUtils.getPinyin(name);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	@Override
	public int compareTo(Person another) {
		return this.pinyin.compareTo(another.getPinyin());
	}

}
