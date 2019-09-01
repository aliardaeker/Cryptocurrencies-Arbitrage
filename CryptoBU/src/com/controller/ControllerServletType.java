package com.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.model.CryptoCurrency;
//import com.model.RefreshData;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class ControllerServletType
 */
@WebServlet("/ControllerServletType")
public class ControllerServletType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerServletType() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MongoClient database = (MongoClient) request.getServletContext().getAttribute("db_client");
		
		
		ArrayList<String> pairs=new ArrayList<String>();
		pairs.add("BTC/USD");
		pairs.add("LTC/BTC");
		pairs.add("ETH/BTC");
		pairs.add("BTC/EUR");
		pairs.add("LTC/EUR");
		pairs.add("LTC/USD");
		pairs.add("ETH/USD");
		pairs.add("ETH/EUR");
		pairs.add("BCH/EUR");
		pairs.add("BCH/USD");
		pairs.add("BCH/BTC");
		pairs.add("XRP/USD");
		pairs.add("XRP/BTC");
		pairs.add("XRP/ETH");
		pairs.add("DASH/ETH");
		pairs.add("DASH/BTC");
		pairs.add("DASH/USD");
		pairs.add("DASH/EUR");
		

		

		ArrayList<CryptoCurrency>  currency_holder = new ArrayList<CryptoCurrency> () ;


		for (String pair : pairs)
		{
			System.out.println(pair);
			DB exchange_db = database.getDB("exchangedb_2");
			DBCollection collection = exchange_db.getCollection("exchange_deneme");
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("pair",pair);

			DBCursor cursor = collection.find(searchQuery).sort(new BasicDBObject("date", -1));
//			cursor.sort(new BasicDBObject("$natural ", -1));

			while (cursor.hasNext()) 
			{
				CryptoCurrency currency3=convertRowtoCryptoCurrency(cursor.next());
				ArrayList<CryptoCurrency>  multiple_currsS3 =getCurrency(currency3);
				for (CryptoCurrency curr : multiple_currsS3)
				{
					currency_holder.add(curr);
				}
				
			}
		}
   		
   		
		// set combined market and pair to dedup easier.. not the dedup holder below .. think you'll remember..
		for (CryptoCurrency curr : currency_holder)
		{
			curr.market_pair_combined=curr.getMarketPlace()+curr.getPair();
		}
		
		ArrayList <CryptoCurrency> unique_ones= new ArrayList<CryptoCurrency>();
		HashMap market_pair_combined_map = new HashMap();
		
		for (CryptoCurrency curr : currency_holder)
		{
			String value = (String) market_pair_combined_map.get(curr.market_pair_combined);
			if (value != null) {
			   continue;
			} else {
				market_pair_combined_map.put(curr.market_pair_combined,"somethings");
				unique_ones.add(curr);
				
			}
		}
   		
   		
   		
   		
   		
   		
   		
   		ArrayList <CryptoCurrency> cp=unique_ones;
   		
   		
   		
   		
   		
   		
   		
   		
   		// TODO Auto-generated method stub
   		HttpSession session=request.getSession();  
   		String curr_name=(String) session.getAttribute("name");

   		ArrayList <CryptoCurrency> cp_filtered= new ArrayList <CryptoCurrency>() ;
		for (CryptoCurrency curr : cp)
		{
			if(curr.getName().equals(curr_name))
			{
				cp_filtered.add(curr);
			}
		}

   		String json = new Gson().toJson(cp_filtered);
           
           response.setContentType("application/json");
           response.getWriter().write(json);
   		
   		
   	}

   	/**
   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   	 */
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   		// TODO Auto-generated method stub
   		doGet(request, response);
   	}
   	
	public  CryptoCurrency convertRowtoCryptoCurrency(DBObject row) {
		CryptoCurrency currency = new CryptoCurrency();
		currency.setPrice((Double) row.get("price"));
		currency.setMarketPlace((String) row.get("market"));
		currency.setPair((String) row.get("pair"));
		
		Date date_to_convert=((Date) row.get("date")); 
		LocalDate date = date_to_convert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		currency.setDate(date);
		
//		System.out.println(currency.toString());
		return currency;

	}
	
	public ArrayList<CryptoCurrency> getCurrency(CryptoCurrency currency ) {
		HashMap<String,String> currency_code_mapping=new HashMap<String,String>(){
			{put("BTC","Bitcoin");
			put("LTC","Litecoin");
			put("ETH","Ethereum");
			put("BCH","Bitcoin Cash");
			put("RP","Ripple");
			put("DASH","Dash");}
		};
		ArrayList<CryptoCurrency> currency_list=new ArrayList<CryptoCurrency>();
		for(String key : currency_code_mapping.keySet()) {
			if(currency.getPair().contains(key)) {
				CryptoCurrency c=new CryptoCurrency();
				c.setMarketPlace(currency.getMarketPlace());
				c.setPair(currency.getPair());
				c.setPrice(currency.getPrice());
				
				System.out.println("Currency="+key);
				c.setName(currency_code_mapping.get(key));
				currency_list.add(c);
			}
		}
		
		return currency_list;
	}

   }

//
//package com.controller;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.google.gson.Gson;
//import com.model.CryptoCurrency;
////import com.model.RefreshData;
//
///**
// * Servlet implementation class ControllerServletType
// */
//@WebServlet("/ControllerServletType")
//public class ControllerServletType extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public ControllerServletType() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//    
//   	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//   		// TODO Auto-generated method stub
//   		HttpSession session=request.getSession();  
////   		RefreshData rd=new RefreshData();
//   		ArrayList <CryptoCurrency> cp=(ArrayList<CryptoCurrency>) session.getAttribute("currency_holder");
//   		String curr_name=(String) session.getAttribute("name");
//
//   		ArrayList <CryptoCurrency> cp_filtered= new ArrayList <CryptoCurrency>() ;
//		for (CryptoCurrency curr : cp)
//		{
//			if(curr.getName().equals(curr_name))
//			{
//				cp_filtered.add(curr);
//			}
//		}
//
//   		String json = new Gson().toJson(cp_filtered);
//           
//           response.setContentType("application/json");
//           response.getWriter().write(json);
//   		
//   		
//   	}
//
//   	/**
//   	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//   	 */
//   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//   		// TODO Auto-generated method stub
//   		doGet(request, response);
//   	}
//
//   }