package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.model.CryptoCurrency;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.model.ChartData;
import com.model.CryptoCurrency;
/**
 * Servlet implementation class DummyChartData
 */
@WebServlet("/DummyChartData")
public class ChartDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChartDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
   		
		//hardcoded pairs.
		// reason we did harcoded is that we have experience with cryptocurrency trading, based on our experience these are the most fluctuated pairs over time.
		// which means more profit and more arbitrage oppurtunities.
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
		
		
		// session object is used to hold passed pairs from outer level Servlet.
		HttpSession session=request.getSession();  

   		
   		// all the most recent documents from db.
   		ArrayList <CryptoCurrency> cp=(ArrayList<CryptoCurrency>) session.getAttribute("currency_holder");
   		// get the corresponding cryptocurrency name for the given servlet.
   		String curr_name=(String) session.getAttribute("name");

   		// for each x axis of chart store in chartdata
   		ArrayList<ChartData> chartdata=new ArrayList<ChartData>();
   		
   		// this loop is to to calculate each pair's max, min and then computing the average for given cryptocurrency.
		for (String pair: pairs)
		{
	   		ArrayList <CryptoCurrency> cp_filtered= new ArrayList <CryptoCurrency>();

			for (CryptoCurrency curr : cp)
			{
				if(curr.getName().equals(curr_name) && curr.getPair().equals(pair))
				{
					cp_filtered.add(curr);
				}
			}
			
			// if there is entry for given pair and given cryptocurrency
			if(cp_filtered.size()>0)
			{
				double min=cp_filtered.get(0).getPrice();
				double max=cp_filtered.get(0).getPrice();
				for (CryptoCurrency cpp: cp_filtered)
				{
				    if(cpp.getPrice() < min) min = cpp.getPrice();
				    if(cpp.getPrice() > max) max = cpp.getPrice();
				}
				// after calculating max min, get the profit calculated by this formula
				chartdata.add(new ChartData(cp_filtered.get(0).getPair(),((max-min)/min)*100,"#c44dff"));
			}
			System.out.println(pair);
		}
		// this hash holds dynamic values for each pair 
		// each value corresponds to different color.
		ArrayList<String> hash_holder = randomStringGenerator(chartdata.size());
		
		for (int i=0 ; i < hash_holder.size();i++)
		{
			chartdata.get(i).color=hash_holder.get(i);
		}

		// make arraylist to json
		String json = new Gson().toJson(chartdata);
        
        response.setContentType("application/json");
        // forwarding...
        response.getWriter().write(json);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	// get dynamic color values for each pair.
	public ArrayList<String> randomStringGenerator(int how_many)
	{
		HashMap<Integer,String> hash_mapper=new HashMap<Integer,String>();
		
		hash_mapper.put(1, "1");
		hash_mapper.put(2,"2");
		hash_mapper.put(3,"3");
		hash_mapper.put(4,"4");
		hash_mapper.put(5,"5");
		hash_mapper.put(6,"6");
		hash_mapper.put(7,"7");
		hash_mapper.put(8,"8");
		hash_mapper.put(9,"9");
		hash_mapper.put(10,"0");
		hash_mapper.put(11,"a");
		hash_mapper.put(12,"b");
		hash_mapper.put(13,"c");
		hash_mapper.put(14,"d");
		hash_mapper.put(15,"e");
		hash_mapper.put(16,"e");

		String string_to_build="";
		ArrayList<String> hash_holder= new ArrayList<String>();
		for (int j=0; j<how_many;j++)
		{
			string_to_build="";

			for (int i=0; i<6; i++)
			{

				Random rn = new Random();
				int rand=rn.nextInt(16);
				string_to_build=string_to_build+hash_mapper.get(rand);
			}
			string_to_build="#"+string_to_build;
			hash_holder.add(string_to_build);
		}

		return hash_holder;
		
	}
	// not used explicitly.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}