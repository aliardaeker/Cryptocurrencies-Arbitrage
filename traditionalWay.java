public void fetchMarketCodes() throws Exception
	{
		Entity <String> payload = Entity.json("");
		
		// Fetching exch_codes which mean the market place not the pair
		Response response = client.target("https://api.coinigy.com/api/v1/exchanges")
								  .request(MediaType.APPLICATION_JSON_TYPE)
								  .header("X-API-KEY", "50a5bfe0eea7072432d65d7d5051a660")
								  .header("X-API-SECRET", "680d996e9caaa5abcae23cafa4fd773c")
								  .post(payload);
		
		if (response.getStatus() == 200)
		{
			String str = response.readEntity(String.class);
			JSONObject json = (JSONObject) new JSONParser().parse(str);
			JSONArray data = (JSONArray) json.get("data");
				
			int size = data.size();
			
			for (int i = 0; i < size; i++)
			{
				JSONObject body = (JSONObject) data.get(i);
				String code = (String) body.get("exch_code");
				marketCodes.add(code);
			}
		}
		else {} // System.out.println("Error in Fetching Market Codes: Status: " + response.getStatus());
	}
	
public void fetchPairsPerMarket() throws Exception
	{
		Entity <String> payload; 
		Response response;
		String marketName, request;
		
		// Fetch pairs for every market available
		// marketNum is used instead of marketCodes.size() for testing
		// Just first marketNum market places are used
		// i = i + 0 is for not to increment the loop if the API call fails
		// Loops only if the response type is 200
		for (int i = 0 ; i < marketCodes.size(); i = i + 0)
		{
			marketName = marketCodes.get(i);
			//marketName = "GDAX";
			
			request = "{\"exchange_code\":" + marketName + "}";
			payload = Entity.json(request);
	
			// Fetching exchange pairs available at the given market place such as "GDAX"
			response = client.target("https://api.coinigy.com/api/v1/markets")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .header("X-API-KEY", "50a5bfe0eea7072432d65d7d5051a660")
				  .header("X-API-SECRET", "680d996e9caaa5abcae23cafa4fd773c")
				  .post(payload);
		
		
			if (response.getStatus() == 200)
			{
				i++;
				String str = response.readEntity(String.class);
				JSONObject json = (JSONObject) new JSONParser().parse(str);
				JSONArray data = (JSONArray) json.get("data");
				
				printMarket.print(marketName);
				int size = data.size();
				
				// pairNum is used instead of size which is number of pairs per market place 
				// First pairNum pairs is used for testing. Much of the pairs are not found at many market places
				for (int j = 0; j < size; j++)
				{
					JSONObject body = (JSONObject) data.get(j);
					String pair = (String) body.get("mkt_name");
						
					if (targetPairs.contains(pair) && !pairsPerMarket.contains(pair))
					{
						if(!APIcall.fetchData(marketName, pair)) j--;
						
						//if (!fetchMarketData(marketName, pair)) j--;
						else pairsPerMarket.add(pair);
					}
				}
				
				pairsPerMarket.clear();
				System.out.println("\n--------------------");
			}
			else {} //System.out.println("Error in Fetching Pairs: Status: " + response.getStatus());
		}	
	}
	
public boolean fetchMarketData(String marketPlaceName, String pair) throws Exception 
	{
		// GDAX: market place name
		// BTC/USD: pair of bit coin and US dollar
		// Asks: selling orders. Minimum price a seller is willing to receive
		// Bids: buying orders. Maximum price a buyer is willing to pay
		// Orders: include both asks and bids
		
		String request = "{\"exchange_code\":\"" + marketPlaceName + "\",\"exchange_market\":\"" + pair + "\",\"type\":\"orders\"}";
		Entity <String> entity = Entity.json(request);
		
		//String request = "{\"exchange_code\":\"BITS\", \"exchange_market\":\"BTC/BTC\", \"type\":\"orders\"}";
		//Entity <String> entity = Entity.json(request);
		
		Response response = client.target("https://api.coinigy.com/api/v1/data")
							      .request(MediaType.APPLICATION_JSON_TYPE)
							      .header("X-API-KEY", "50a5bfe0eea7072432d65d7d5051a660")
							      .header("X-API-SECRET", "680d996e9caaa5abcae23cafa4fd773c")
							      .post(entity);
		
		if (response.getStatus() == 200)
		{
			String str = response.readEntity(String.class);
			JSONObject json = (JSONObject) new JSONParser().parse(str);
			JSONObject data = (JSONObject) json.get("data");
			
			//System.out.println("body:" + data.toString());
			
			//JSONArray asks = (JSONArray) data.get("asks");
			JSONArray bids = (JSONArray) data.get("bids");
			
			//int size_asks = asks.size();
			int size_bids = bids.size();
			
			/*
			if (size_asks == 0) {} //System.out.println("No selling order found for " + pair + " in " + marketPlaceName);
			else
			{
				for (int i = 0; i < size_asks; i++)
				{
					JSONObject price = (JSONObject) asks.get(i);
					String p_str = (String) price.get("total");
					double p = Double.parseDouble(p_str);
					prices.add(p);
				}
				
				printOrder.print("Ask");
				//System.out.println("\nSelling Orders (Asks) :");
				//getAvgTraditional(marketPlaceName, pair);
				getAvgFunctional(marketPlaceName, pair);
				prices.clear();
			}
			*/
			
			if (size_bids == 0) {} //System.out.println("No buying order found for " + pair + " in " + marketPlaceName);
			else 
			{
				for (int i = 0; i < size_bids; i++)
				{
					JSONObject price = (JSONObject) bids.get(i);
					String p_str = (String) price.get("total");
					double p = Double.parseDouble(p_str);
					prices.add(p);
				}
			
				printOrder.print("Bid");
				//getAvgTraditional(marketPlaceName, pair);
				
				prices = prices.stream()
					  .sorted((p1, p2) -> Double.compare(p2, p1))
					  .limit(5)
					  .collect(Collectors.toList());
				
				//prices.stream().forEach(p1 -> System.out.print("- " + p1));
				
				getAvgFunctional(marketPlaceName, pair);
				prices.clear();
			}
			
			return true;
		}
		else return false; //System.out.println("Error in Fetching Market Data. Status: " + response.getStatus());		
	}


public void getAvgFunctional(String marketPlaceName, String pair)
	{
		Stream <Double> priceStream = prices.parallelStream();
		
		double sum = priceStream.reduce(0.0, Double::sum);
		double res = operate(sum, prices.size(), division);
		
		printAvg.print(pair, marketPlaceName, res);
	}