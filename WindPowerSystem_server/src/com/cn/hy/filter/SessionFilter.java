package com.cn.hy.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 过滤器
 * @author Administrator
 *
 */
public class SessionFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		String employeeId=(String) session.getAttribute("employeeId");
		res.setContentType("text/html;charset=UTF-8");
		req.setCharacterEncoding("UTF-8"); 
		String name=req.getContextPath();
		if(employeeId == null){
			String[] excludeFiles = {"/WindPowerSystem_server/login.jsp","/login",".css",".jpg",".png",".gif",".bmp"};
			String requestUri = req.getRequestURI();
			boolean flag = false;
			for (String file : excludeFiles) {
				if(requestUri.endsWith(file)){
					flag = true;
					break;
				}
			}
			if(flag){
				chain.doFilter(request, response);
			}else{
				
				PrintWriter out = res.getWriter();
				out.println("<script>alert('您没有长时间操作电脑，请重新登录!');top.location.href='"+name+"/login.jsp';</script>");
				out.close();
			}
		}else{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
