package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.TicketMasterAPI;
import database.DBConnection;
import database.DBConnectionFactory;
import entity.Item;

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
		// allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		
		// term can be empty
		System.out.println("Did i go here1?");
		String term= "";
		String userId = session.getAttribute("user_id").toString(); 
//		String userId = request.getParameter("user_id");

		// default: mysql
		DBConnection connection = DBConnectionFactory.getConnection();
		Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
		System.out.println("Did i go here2?");
        try {
        	List<Item> items = connection.searchItems(lat, lon, term);
        	JSONArray array = new JSONArray();
        	System.out.println("Did i go here3?");
        	for (Item item : items) {
        		System.out.println("Did i go here4?");
        		JSONObject obj = item.toJSONObject();
        		// decide whether to show an empty or solid heart
				obj.put("favorite", favoritedItemIds.contains(item.getItemId()));

        		array.put(obj);
        	}
        	rpcHelper.writeJsonArray(response, array);
 
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
