package com.chj.tencent.index.bean;

import com.chj.tencent.util.PinyinUtils;

/**
 * 
 * @包名:com.chj.tencent.index.bean
 * @类名:Person
 * @作者:陈火炬
 * @时间:2015-8-20 上午10:53:35
 * 
 * @描述:一个java的bean
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
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
