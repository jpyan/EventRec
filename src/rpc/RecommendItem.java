package rpc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class RecommendItem
 */
@WebServlet("/recommendation")
public class RecommendItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecommendItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		JSONArray array = new JSONArray();
//
//		try {
//			array.put(new JSONObject().put("username", "abcd").put("address", "San Francisco").put("time", "01/01/2017"));
//			array.put(new JSONObject().put("username", "1234").put("address", "San Jose").put("time", "01/02/2017"));
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//		out.print(array);
//		out.close();	
		
		
		JSONArray array = new JSONArray();
		try {
			array.put(new JSONObject().put("username", "zxcv").put("address", "SF").put("time", "01/01/2019"));
			array.put(new JSONObject().put("username", "asdf").put("address", "LA").put("time", "09/01/2019"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		rpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
