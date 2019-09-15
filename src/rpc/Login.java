package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import database.DBConnection;
import database.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String userId = request.getParameter("user_id");
//		String password = request.getParameter("password");
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			// If () is false and the request has no valid HttpSession, this method returns null.
			HttpSession session = request.getSession(false); 
			JSONObject obj = new JSONObject();
			if (session != null) {
				String userId = session.getAttribute("user_id").toString();
				obj.put("result", "SUCCESS").put("user_id", userId).put("name", conn.getFullname(userId));
			} else {
				response.setStatus(403);
				obj.put("result", "Invalid Session");
			}
			rpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			JSONObject input = rpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String password = input.getString("password");
			JSONObject obj = new JSONObject();
			System.out.println("user: " + userId);
			System.out.println("password: " + password);
			if (conn.verifyLogin(userId, password)) {
				HttpSession session = request.getSession();
				session.setAttribute("user_id", userId);
				obj.put("result", "SUCCESS").put("user_id", userId).put("name", conn.getFullname(userId));
			} else {
				System.err.println("2.pass here");
				response.setStatus(401);
				obj.put("result", "User Doesn't Exist");
			}
			rpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

}
