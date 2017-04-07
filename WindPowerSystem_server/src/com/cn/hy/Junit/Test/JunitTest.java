package com.cn.hy.Junit.Test;

import java.util.List;

import com.cn.hy.dao.devicesystem.DeviceSystemDao;
import com.cn.hy.dao.devicesystem.DeviceSystemTestDao;
import com.cn.hy.dao.modbustcp.ModBusTcpDao;
import com.cn.hy.dao.system.RoleDao;
import com.cn.hy.dao.system.UserDao;
import com.cn.hy.pojo.modbustcp.ModBusParame;
import com.cn.hy.pojo.modbustcp.ModbusBit;
import com.cn.hy.pojo.system.Menu;
import com.cn.hy.pojo.system.User;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试
 * @author Administrator
 *
 */
public class JunitTest {
	UserDao userdao;
	ModBusTcpDao modbusdao;
	RoleDao roledao;
	DeviceSystemDao devicesystemdao;
	DeviceSystemTestDao devicesystemtestdao;

	@Before
	public void before() {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "classpath:xmlconfig/spring-mybatis.xml" });

		userdao = context.getBean(UserDao.class);
		modbusdao = context.getBean(ModBusTcpDao.class);
		roledao = context.getBean(RoleDao.class);
		devicesystemdao=context.getBean(DeviceSystemDao.class);
		devicesystemtestdao=context.getBean(DeviceSystemTestDao.class);
	}

	// @Test
	public void test1() {
		User user = userdao.selectUser("001", "e10adc3949ba59abbe56e057f20f883e");
		System.out.println(user.getMenu().get(0).getRead_p());
	}

	// @Test
	public void test2() {
		List<Menu> me = userdao.selectmenubyuserid(1);
		System.out.println(me.size());
	}

	//@Test
	public void test3() {
		User user = new User();
		user.setName("bbb");
		user.setPasswd("2");
		user.setEmployeeId("3");
		user.setEmployeeName("4");
		userdao.insertUser(user);
		System.out.println(user.getId());
	}
	//@Test
	public void test4() {
		User user=new User();
		user.setEmployeeId("111");
		user.setEmployeeName("cess");
		user.setName("121");
		user.setPasswd("110");
		user.setId(26);
		user.setState(0);
	   userdao.updateUser(user);
	}
	//@Test
	public void test5() {
		userdao.deleteUser(51);
	
	}
	//@Test
	public void test6() {
		ModBusParame mbparame=new ModBusParame();
		mbparame.setAddr(22);
		mbparame.setDevice_type(0);
		mbparame.setShortvalue(-596);
		List<ModbusBit> bitlist=modbusdao.getaddrbitlist(mbparame);
		String bitstr="";
		String bstr=Integer.toBinaryString(mbparame.getShortvalue());
		
		int lgth=bstr.length();
		if(lgth>16){
			bstr=bstr.substring(bstr.length()-16, bstr.length());
			lgth=bstr.length();
		}
		System.out.println(bstr);
		for(int i=lgth-1;i>=0;i--){
			int state=Integer.parseInt((bstr.substring(i, i+1)));
			System.out.println(state);
			ModbusBit bitdemo= bitlist.get(bstr.length()-i-1);
			bitdemo.setShowstate(state);
			if(lgth>15){
			if(i==0){
				bitstr+=bitdemo.toString();
				}else{
					bitstr+=bitdemo.toString()+"|";
				}
			}else{
				bitstr+=bitdemo.toString()+"|";
			}
			
		}
		   for(int j=lgth;j<bitlist.size();j++){
			ModbusBit bitdemonew= bitlist.get(j);
			bitdemonew.setShowstate(0);
			if(j==bitlist.size()-1){
				bitstr+=bitdemonew.toString();
				}else{
				bitstr+=bitdemonew.toString()+"|";
				}
		   }
		   
		   
		   System.out.println(bitstr);
	}
	
	@Test
	public void test8() {
		
		System.out.println(devicesystemtestdao.getControlModelTestList(0, 2).size());
		
		} 

}
