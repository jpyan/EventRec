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
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
//		if (request.getParameter("username") != null) {
//			String username = request.getParameter("username");
//			
//			JSONObject obj = new JSONObject();
//			
//			try {
//				obj.put("username", username);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			out.print(obj);
//		}
		
		JSONArray jsonArray = new JSONArray();
		
		try {
			jsonArray.put(new JSONObject().put("username", "1234"));
			jsonArray.put(new JSONObject().put("username", "5678"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		rpcHelper.writeJsonArray(response, jsonArray);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
