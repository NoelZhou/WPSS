package com.cn.hy.pojo.trendchart;

import java.util.List;

public class Series {
	public String name;  
    
    public String type;  
      
    public List<Integer> data;//这里要用int 不能用String 不然前台显示不正常（特别是在做数学运算的时候）  

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Integer> getData() {
		return data;
	}
	public void setData(List<Integer> data) {
		this.data = data;
	}
}
