package com.model;

import java.time.LocalDate;

public class CryptoCurrency {
	private String name,market_place,pair;
	private double price,avg_price;
	private LocalDate date;
	public String market_pair_combined;
	public CryptoCurrency() {
		name=market_place=pair="nil";
		price=avg_price=0;
		market_pair_combined="";
	}
	public void setPrice(double price) {
		this.price=price;
	}
	public void setAvgPrice(double avg_price) {
		this.avg_price=avg_price;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setMarketPlace(String market_place) {
		this.market_place=market_place;
	}
	public void setPair(String pair) {
		this.pair=pair;
	}
	public String getName() {
		return this.name;
	}
	public String getMarketPlace() {
		return this.market_place;
	}
	public String getPair() {
		return this.pair;
	}
	public double getPrice() {
		return this.price;
	}
	public double getAvgPrice() {
		return this.avg_price;
	}
	

	@Override
	public String toString() {
		return (this.name+" "+this.market_place+" "+this.pair+" "+this.price+" "+this.avg_price);
	}
	public void setDate(LocalDate date) {
		this.date=date;
		
	}

	
	
}
