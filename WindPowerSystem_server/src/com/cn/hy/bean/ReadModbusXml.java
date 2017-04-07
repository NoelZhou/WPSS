package com.cn.hy.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadModbusXml {
	private  List<String> ModBusTcpSystemParamelist = new ArrayList<String>();
	/**
	 * 解析文件xml
	 * 
	 * @param xmlpath
	 */
	public List<String> setlist(String xmlpath) {
		SAXReader reader = new SAXReader();
		List<String> liststr=new ArrayList<String>();
		try {
			File file = new File(xmlpath);
			Document document;
			System.out.println(xmlpath);
			document = reader.read(file);
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> childElements = root.elements();
			// 获取解析定义格式
			liststr=getList(childElements); 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return  liststr;
	}

	/**
	 * 获取协议定义
	 * 
	 * @param childElements
	 */
	public  List<String> getList(List<Element> childElements) {
		try {

			// 计算节点中的属性值
			// 计算几点数
			for (Element child : childElements) {
				String listr = "";
				// 未知属性名情况下
				@SuppressWarnings("unchecked")
				List<Attribute> attributeList = child.attributes();
				// 主节点中的属性值
				for (int i = 0; i < attributeList.size(); i++) {
					// System.out.println(attr.getName() + ": " +
					// attr.getValue());
					if (i == attributeList.size() - 1) {
						listr += attributeList.get(i).getValue();
					} else {
						listr += attributeList.get(i).getValue() + ",";
					}

				}
				@SuppressWarnings("unchecked")
				List<Element> elementList = child.elements();
				if (elementList.size() == 0 && child.getQName().getName().equals("list")) {
					// System.out.println(child.getText());
					listr += "," + child.getText();
					listr += ",自定义值";
				}
				if (child.getQName().getName().equals("point") || child.getQName().getName().equals("device")) {
					ModBusTcpSystemParamelist.add(listr);
				} 
				// 主节点下面的，应数据库片接处理，
				if (elementList.size() != 0) {
					getList(elementList);
				}
			} 
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return  ModBusTcpSystemParamelist;
	}
}
