package CoinTrader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.json.simple.parser.*;
import org.json.simple.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.LocalDateTime;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ComputePrice
{	
	private List <Double> prices;
	private List <String> marketCodes;
	private List <String> marketNames;	
	
	// For debugging only and not to fetch a pair twice
	private List <String> pairsPerMarket;
	
	// Program fetches all the markets and pairs from coinigy API
	// Most of them do not have good amount of data so lets focus
	// on the target markets and pairs
	private List <String> targetPairs;
	private List <String> targetMarkets;
	
	private List <ExchangeData> exchangeItems;
	private Client client;
	
	public ComputePrice()
	{
		client = ClientBuilder.newClient();
		
		prices = new ArrayList <Double> ();
		marketCodes = new ArrayList <String> ();
		pairsPerMarket = new ArrayList <String> ();
		targetPairs = new ArrayList <String> ();
		targetMarkets = new ArrayList <String> ();
		exchangeItems = new ArrayList <ExchangeData> ();
		marketNames = new ArrayList <String> ();
		
		// Excluding pairs which are not listed here
		targetPairs.add("BTC/USD");
		targetPairs.add("BCH/USD");
		targetPairs.add("BCH/BTC");
		targetPairs.add("XRP/USD");
		targetPairs.add("XRP/BTC");
		targetPairs.add("XRP/BCH");
		targetPairs.add("XRP/ETH");
		targetPairs.add("DASH/ETH");
		targetPairs.add("DASH/BCH");
		targetPairs.add("DASH/LTC");
		targetPairs.add("DASH/BTC");
		targetPairs.add("DASH/USD");
		targetPairs.add("DASH/EUR");
		
		// Excluding market places which are not listed here
		targetMarkets.add("BIST");
		targetMarkets.add("BITF");
		targetMarkets.add("HITB");
		targetMarkets.add("CXIO");
		targetMarkets.add("GATE");
		targetMarkets.add("EXMO");
		targetMarkets.add("GDAX");
		targetMarkets.add("LIVE");
		targetMarkets.add("BINA");
		targetMarkets.add("BTRX");
		targetMarkets.add("PLNX");
		targetMarkets.add("ROCK");
		targetMarkets.add("CBNK");
	}
	
	MathOperation division = (double a, double b) -> a / b;
	Communicate printMarket = (message) -> System.out.println("Market Name : " + message);
	Communicate printOrder = (message) -> System.out.println(message + " Orders :");
	Communicate3 printCounter = (message) -> System.out.println("************************ Core Loop " + message + " ************************");
	Communicate2 printAvg = (m1, m2, m3) -> System.out.println("  --> Average Price for " + m1 + " in " + m2 + " is " + m3);
	
	// Lambda for average calculation
	AVG avgCalculate = (String marketPlaceName, String pair) ->
	{
		Stream <Double> priceStream = prices.stream();
		
		// Reduce operation
		double sum = priceStream.reduce(0.0, Double::sum);
		// Using division lambda 
		double res = operate(sum, prices.size(), division);
		
		printAvg.print(pair, marketPlaceName, res);
		// Item will be committed
		ExchangeData item = new ExchangeData(marketPlaceName, pair, res, LocalDateTime.now());
		exchangeItems.add(item);
	};
	
	// Given market and pair fetches the orders
	API_Data fetchOrders = (market, pair, name) -> 
	{
		// Retrieve the orders at given market place with given pair
		String request = "{\"exchange_code\":\"" + market + "\",\"exchange_market\":\"" + pair + "\",\"type\":\"orders\"}";
		Entity <String> entity = Entity.json(request);
		Response response = null;
		
		try 
		{
			response = client.target("https://api.coinigy.com/api/v1/data")
			      .request(MediaType.APPLICATION_JSON_TYPE)
			      .header("X-API-KEY", "50a5bfe0eea7072432d65d7d5051a660")
			      .header("X-API-SECRET", "680d996e9caaa5abcae23cafa4fd773c")
			      .post(entity);
		} 
		catch (Exception e) {e.printStackTrace();}

		if (response.getStatus() == 200)
		{
			String str = response.readEntity(String.class);
			JSONObject json = null;
			
			try 
			{json = (JSONObject) new JSONParser().parse(str);}
			catch (ParseException e)
			{e.printStackTrace();}
			
			JSONObject data = (JSONObject) json.get("data");

			JSONArray bids = (JSONArray) data.get("bids");
			int size_bids = bids.size();

			if (size_bids == 0) {} //System.out.println("No buying order found for " + pair + " in " + marketPlaceName);
			else 
			{
				@SuppressWarnings("unchecked")
				Stream <JSONObject> s = bids.stream().map(j -> (JSONObject) j);
				
				// Traverse JSON and convert JSON objects with map and collect
				prices = s.map(b -> Double.parseDouble((String) b.get("total"))).collect(Collectors.toList ());

				printOrder.print("Bid");

				// Get the max 5 element with stream sorted and limit
				prices = prices.stream()
						.sorted((p1, p2) -> Double.compare(p2, p1))
						.limit(5)
						.collect(Collectors.toList());

				// prices.stream().forEach(p1 -> System.out.print("- " + p1));

				// Calculate average with functional
				avgCalculate.avg(market, pair);
				prices.clear();
			}

			return true;
		}
		else return false; //System.out.println("Error in Fetching Market Data. Status: " + response.getStatus());	
	};
	
	// Fetches the markets
	API_Market fetchMarkets = () ->
	{
		Entity <String> payload = Entity.json("");
		Response response = null;
		
		try 
		{
			// Fetching exch_codes which mean the market place not the pair
			response = client.target("https://api.coinigy.com/api/v1/exchanges")
								  .request(MediaType.APPLICATION_JSON_TYPE)
								  .header("X-API-KEY", "50a5bfe0eea7072432d65d7d5051a660")
								  .header("X-API-SECRET", "680d996e9caaa5abcae23cafa4fd773c")
								  .post(payload);
		}
		catch (Exception e) {e.printStackTrace();}
		
		if (response.getStatus() == 200)
		{
			String str = response.readEntity(String.class);
			JSONObject json = null;
			
			try {json = (JSONObject) new JSONParser().parse(str);} 
			catch (ParseException e) {e.printStackTrace();}
			
			JSONArray data = (JSONArray) json.get("data");
		
			// Parsing JSON Array with map and collect
			
			@SuppressWarnings("unchecked")
			Stream <JSONObject> s1 = data.stream().map(j -> (JSONObject) j);
			@SuppressWarnings("unchecked")
			Stream <JSONObject> s2 = data.stream().map(j -> (JSONObject) j);
			
			marketCodes = s1.map(b -> (String) b.get("exch_code")).collect(Collectors.toList ());
			marketNames = s2.map(b -> (String) b.get("exch_name")).collect(Collectors.toList ());
		}
		else {} // System.out.println("Error in Fetching Market Codes: Status: " + response.getStatus());
	};
	
	// Fetches the pairs
	API_Pair fetchPairs = () ->
	{
		Entity <String> payload; 
		Response response = null;
		String marketName, request, marketNameReal;
		
		// Fetch pairs for every market available at targeted markets
		// i = i + 0 is for not to increment the loop if the API call fails
		// Loops only if the response type is 200
		for (int i = 0 ; i < marketCodes.size(); i = i + 0)
		{
			marketName = marketCodes.get(i);
			marketNameReal = marketNames.get(i);
			//marketName = "GDAX";
			
			// If we do not target a market skip it.
			if (!targetMarkets.contains(marketName)) 
			{
				i++;
				continue;
			}
			
			request = "{\"exchange_code\":" + marketName + "}";
			payload = Entity.json(request);
	
			try 
			{
				// Fetching exchange pairs available at the given market place such as "GDAX"
				response = client.target("https://api.coinigy.com/api/v1/markets")
						.request(MediaType.APPLICATION_JSON_TYPE)
						.header("X-API-KEY", "50a5bfe0eea7072432d65d7d5051a660")
						.header("X-API-SECRET", "680d996e9caaa5abcae23cafa4fd773c")
						.post(payload);
			}
			catch (Exception e) {e.printStackTrace();;}
		
		
			if (response.getStatus() == 200)
			{
				i++;
				String str = response.readEntity(String.class);
				JSONObject json = null;
				
				try {json = (JSONObject) new JSONParser().parse(str);} 
				catch (ParseException e) {e.printStackTrace();}
				
				JSONArray data = (JSONArray) json.get("data");
				
				System.out.println("--------------------");
				printMarket.print(marketNameReal);
				int size = data.size();
				int fallbackCounter = 0;
				
				for (int j = 0; j < size; j++)
				{
					JSONObject body = (JSONObject) data.get(j);
					String pair = (String) body.get("mkt_name");
						
					if (targetPairs.contains(pair) && !pairsPerMarket.contains(pair))
					{
						if(!fetchOrders.fetchData(marketName, pair, marketNameReal)) 
						{
							j--;
							fallbackCounter++;
							
							// If the response is not 200 more than 4 times, discard that pair
							if (fallbackCounter == 4) 
							{
								fallbackCounter = 0;
								j++;
							}
						}
						else 
						{
							fallbackCounter = 0;
							pairsPerMarket.add(pair);
						}
					}
				}
				
				pairsPerMarket.clear();
				System.out.println("--------------------");
			}
			else {} //System.out.println("Error in Fetching Pairs: Status: " + response.getStatus());
		}	
	};
	
	public static void main(String [] args) throws Exception
	{
		ComputePrice cp = new ComputePrice();
		int counter = 0;
		cp.fetchMarkets.fetchMarketData();
		
		MongoClient database = new MongoClient("localhost");
		DB exchange_db = database.getDB("exchangedb_2");
		
		DBCollection collection = exchange_db.getCollection("exchange_deneme");



		
		while (true)
		{
			cp.exchangeItems.clear();
			cp.printCounter.print(counter);
			counter++;
			
			cp.fetchPairs.fetchPairData();	
			//TimeUnit.SECONDS.sleep(5);
			
			// Update the db
			for (ExchangeData data : cp.exchangeItems)
			{
				DBObject data_db=ExchangetoDBObject(data);
				collection.insert(data_db);

			}

		}
	}
	
	interface Communicate 
	{
	     void print (String str);
	}
	
	interface Communicate2 
	{
		void print (String s1, String s2, double d);
	}
	
	interface Communicate3 
	{
	     void print (int str);
	}
	
	interface MathOperation 
	{
		double operation (double a, double b);
	}
	
	double operate (double a, double b, MathOperation mathOperation) 
	{
		return mathOperation.operation(a, b);
	}
	
	interface API_Data
	{
		boolean fetchData (String marketName, String pair, String name);
	}
	
	interface API_Market
	{
		void fetchMarketData ();
	}
	
	interface API_Pair
	{
		void fetchPairData ();
	}
	
	interface AVG
	{
		void avg (String s1, String s2);
	}
}