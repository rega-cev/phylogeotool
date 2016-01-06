package be.kuleuven.rega.phylogeotool.trash;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test2 extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		super.doPost(req, resp);
		
		System.out.println("GET");
//		System.out.println(req.getParameter("id"));
		System.out.println(req.getParameter("pplacer"));
//		super.doGet(req, resp);
		
		if(req.getParameter("pplacer") != null) {
			System.out.println("Parameter: " + req.getParameter("pplacer"));
		} else {
			System.out.println("Parameter not set");
		}
		
	}
	
	public static void main(String[] args) {
		String test = "/root=1/pplacer_id=1";
		System.out.println(test);
	}
	
}
