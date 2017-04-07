package com.zhta.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhta.data.RunDataTHD;
import com.zhta.data.TextThd;
import com.zhta.data.TextThd.THD_ONE;
import com.zhta.historydata.HistoryDataAcqAction;

@WebServlet("/StopServlet")
public class StopServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 public StopServlet() {
		 super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HistoryDataAcqAction.stopHistoryData();
		RunDataTHD.stopRunData();
		THD_ONE.stopOne();
		TextThd.serviceManager.stopAsync();
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
	

}
