//package com.model;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Random;
//
//public class RefreshData {
//	
//	public CryptoCurrency[] makedata() {
//		int i;
//		CryptoCurrency cp[]=new CryptoCurrency[Constants.no_of_cryptocurrencies];
//		for(i=0;i<cp.length;i++) {
//			cp[i]=new CryptoCurrency();
//			cp[i].setName(Constants.CRYPTO_CURRENCY[i]);
//			double avg_price=Constants.min_range + (Constants.max_range - Constants.min_range) * new Random().nextDouble();
//			DecimalFormat two = new DecimalFormat("#0.00");
////			cp[i].setAvgPrice(Double.parseDouble(two.format(avg_price)));
//			
//		}
//		
//		return cp;
//	}
//	public ArrayList<CryptoCurrency> makedata(String name_of_currency){
//		int i;
//		double price=0;
//		ArrayList<CryptoCurrency> cp=new ArrayList<CryptoCurrency>();
//		for(i=0;i<100;i++) {
//			if(Constants.cryptoData[i][0].equalsIgnoreCase(name_of_currency)) {
//			CryptoCurrency c=new CryptoCurrency();
//			c.setName(name_of_currency);
//			c.setMarketPlace(Constants.cryptoData[i][1]);
//			c.setPair(Constants.cryptoData[i][2]);
//			price=Constants.min_range + (Constants.max_range - Constants.min_range) * new Random().nextDouble();
//			DecimalFormat two = new DecimalFormat("#0.00");
//			c.setPrice(Double.parseDouble(two.format(price)));
//			cp.add(c);
//			}
//		}
//		
//		
//		return cp;
//		
//		
//	}
//}
