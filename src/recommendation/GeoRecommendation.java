
package recommendation;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import database.DBConnection;
import database.DBConnectionFactory;
import database.mysql.MySQLConnection;
import entity.Item;


// Recommendation based on geo distance and similar categories.
public class GeoRecommendation {
	
	public List<Item> recommendItems(String userId, double lat, double lon) {
		List<Item> recItems = new ArrayList<>();
	
		// first, get all favItems' ID
		DBConnection conn = DBConnectionFactory.getConnection();
		Set<String> favIds = conn.getFavoriteItemIds(userId);
		
		// second, get all categories and sort by count
		// {"music": 2, "art": 3}
		Map<String, Integer> allCategories = new HashMap<>();
		for (String itemId : favIds) {
			Set<String> categories = conn.getCategories(itemId);
			for (String cat : categories) {
				allCategories.put(cat, allCategories.getOrDefault(cat, 0) + 1);
			}
		}
		// sort by count (using collections.sort and comparator)
		// {"art": 3, "music": 2}
		List<Entry<String, Integer>> categoryList = new ArrayList<>(allCategories.entrySet());
		Collections.sort(categoryList, (Entry<String, Integer> e1, Entry<String, Integer> e2) -> {
			return Integer.compare(e2.getValue(), e1.getValue());
		});
		
		// third, search based on category, filter our fav Items
		Set<String> visitedItemIds = new HashSet<>();
		for (Entry<String, Integer> cat : categoryList) {
			List<Item> items = conn.searchItems(lat, lon, cat.getKey());
			for (Item item : items) {
				if (!favIds.contains(item.getItemId()) && !visitedItemIds.contains(item.getItemId())) {
					recItems.add(item);
					visitedItemIds.add(item.getItemId());
				}
			}
		}
		
		conn.close();
		return recItems;
		
	}
}
