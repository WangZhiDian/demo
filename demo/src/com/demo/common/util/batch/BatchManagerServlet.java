package com.demo.common.util.batch;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BatchManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		
//		if (getInitParameter("syncBatch") != null) {
//			BatchManager.getInstance().loadSyncBatch(
//					getInitParameter("syncBatch"));
//		}
//
//		if (getInitParameter("asyncBatch") != null) {
//			BatchManager.getInstance().loadAsyncBatch(
//					getInitParameter("asyncBatch"));
//		}
//
//		if (getInitParameter("action") != null
//				&& getInitParameter("action").equalsIgnoreCase("start")) {
//			BatchManager.getInstance().startAllBatch();
//		}
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void destroy() {
		DateFormat d5 = DateFormat.getDateTimeInstance(DateFormat.FULL,
				DateFormat.FULL); // 显示日期，周，时间（精确到秒�?
		Date now = new Date();

		System.out.println("Begin: stop the account system at:"
				+ d5.format(now));
		try {
			BatchManager.getInstance().stopAllBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End: stop the account system at:" + d5.format(now));
	}
}
