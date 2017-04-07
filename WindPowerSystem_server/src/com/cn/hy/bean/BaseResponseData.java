/**
* @author 关龙锋 
* @date : 2016年7月5日 上午10:42:17
*/
package com.cn.hy.bean;

import java.util.Map;
/**
 * 后台传送JSON数据
 * @author Administrator
 *
 */
public class BaseResponseData {
   private int code;
   private String message;
   private Map<?, ?> responseData;
public int getCode() {
	return code;
}
public void setCode(int code) {
	this.code = code;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Map<?, ?> getResponseData() {
	return responseData;
}
public void setResponseData(Map<?, ?> responseData) {
	this.responseData = responseData;
} 
   
   
}
