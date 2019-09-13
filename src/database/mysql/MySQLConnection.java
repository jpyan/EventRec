package database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import api.TicketMasterAPI;
import database.DBConnection;
import entity.Item;
import entity.Item.ItemBuilder;

public class MySQLConnection implements DBConnection {
	
	private Connection conn;
    
    public MySQLConnection() {
    	try {
	   		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
	   		 conn = DriverManager.getConnection(MySQLDBUtil.URL);
	   		 
	   	 } catch (Exception e) {
	   		 e.printStackTrace();
	   	 }
    }
    
    @Override
    public void close() {
	   	 if (conn != null) {
	   		 try {
	   			 conn.close();
	   		 } catch (Exception e) {
	   			 e.printStackTrace();
	   		 }
	   	 }
    }

    @Override
	public List<Item> searchItems(double lat, double lon, String term) {
    	TicketMasterAPI ticketMasterAPI = new TicketMasterAPI();
    	List<Item> items = ticketMasterAPI.search(lat, lon, term);
   	 	for(Item item : items) {
   	 		saveItem(item);
   	 	}
   	 	return items;
	}

	@Override
	public void setFavoriteItems(String userId, List<String> itemIds) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
	  	}
		
		try {
			String sql = "INSERT IGNORE INTO history(user_id, item_id) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			for (String itemId : itemIds) {
				ps.setString(2, itemId);
				ps.execute();
			}		
		} catch (Exception e) {
			e.printStackTrace();
	    }

	}

	@Override
	public void unsetFavoriteItems(String userId, List<String> itemIds) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return;
	  	}
		
		try {
			String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			for (String itemId : itemIds) {
				ps.setString(2, itemId);
				ps.execute();
//				/* test */
//				ResultSet set = ps.getResultSet();
//				System.out.println(set);
			}		
		} catch (Exception e) {
			e.printStackTrace();
	    }
	}

	@Override
	public Set<String> getFavoriteItemIds(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashSet<>();
	  	}
		
		Set<String> favItemIds = new HashSet<>();
		
		try {
			String sql = "SELECT item_id FROM history WHERE user_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				favItemIds.add(rs.getString("item_id"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return favItemIds;
	}

	@Override
	public Set<Item> getFavoriteItems(String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return new HashSet<>();
	  	}
		
		Set<Item> favItems = new HashSet<>();
		Set<String> favItemIds = getFavoriteItemIds(userId);
		
		try {
			String sql = "SELECT * FROM items WHERE item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			for (String itemId : favItemIds) {
				ps.setString(1, itemId);
				
				ResultSet rs = ps.executeQuery();
				
				ItemBuilder builder = new ItemBuilder();
				
				while (rs.next()) {
					builder.setItemId(rs.getString("item_id"));
					builder.setName(rs.getString("name"));
					builder.setAddress(rs.getString("address"));
					builder.setImageUrl(rs.getString("image_url"));
					builder.setUrl(rs.getString("url"));
					builder.setCategories(getCategories(itemId));
					builder.setDistance(rs.getDouble("distance"));
					builder.setRating(rs.getDouble("rating"));
					
					favItems.add(builder.build());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return favItems;
	}

	@Override
	public Set<String> getCategories(String itemId) {
		if (conn == null) {
			return null;
		}
		Set<String> categories = new HashSet<>();
		try {
			String sql = "SELECT category FROM categories WHERE item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, itemId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				categories.add(rs.getString("category"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public void saveItem(Item item) {
        if (conn == null) {
  		   System.err.println("DB connection failed");
  		   return;
  	    }
        // sql injection
        // select * from users where username = '' AND password = '';
  	 
        // username: fakeuser ' OR 1 = 1; DROP  --
        // select * from users where username = 'fakeuser ' OR 1 = 1 --' AND password = '';
	  	try {
	  		// System.out.println("processed");
	  		String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?, ?, ?)";
	  		PreparedStatement ps = conn.prepareStatement(sql);
	  		ps.setString(1, item.getItemId());
	  		ps.setString(2, item.getName());
	  		ps.setDouble(3, item.getRating());
	  		ps.setString(4, item.getAddress());
	  		ps.setString(5, item.getImageUrl());
	  		ps.setString(6, item.getUrl());
	  		ps.setDouble(7, item.getDistance());
	  		ps.execute();
	  		 
	  		sql = "INSERT IGNORE INTO categories VALUES(?, ?)";
	  		ps = conn.prepareStatement(sql);
	  		ps.setString(1, item.getItemId());
	  		for(String category : item.getCategories()) {
	  			ps.setString(2, category);
	  			ps.execute();
	  		}
	  		 
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}

	@Override
	public String getFullname(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
