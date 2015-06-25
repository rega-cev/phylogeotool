package be.kuleuven.rega.treedraw.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class PhyloGeoService extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		System.out.println(req.getParameter("sequence"));
		System.out.println(req.getParameter("gender"));
		System.out.println(req.getParameter("ethnic"));
		System.out.println(req.getParameter("origin"));
		System.out.println(req.getParameter("infection"));
		System.out.println(req.getParameter("birth"));
		System.out.println(req.getParameter("risk"));
		super.doPost(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		
		System.out.println("GET");
		System.out.println(req.getAttribute("sequence"));
		super.doGet(req, resp);
	}
}