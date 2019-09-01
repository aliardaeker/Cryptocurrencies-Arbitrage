package CoinTrader;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ExchangeData 
{
	private String marketName;
	private String exchangePair;
	private double avgPrice;
	private Date timeStamp;

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getExchangePair() {
		return exchangePair;
	}

	public void setExchangePair(String exchangePair) {
		this.exchangePair = exchangePair;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public ExchangeData (String market, String pair, double price, LocalDate date)
	{
		marketName = market;
		exchangePair = pair;
		avgPrice = price;
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
//		Date d = Date.from(instant);
		Date d = new Date();
		timeStamp = d;

	}
}
