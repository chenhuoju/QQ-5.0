package com.itheima.tencentqq.swipe;

import com.itheima.tencentqq.swipe.SwipeLayout.Status;

public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
