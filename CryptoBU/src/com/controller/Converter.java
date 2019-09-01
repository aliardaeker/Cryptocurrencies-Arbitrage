package com.controller;

import java.time.LocalDate;

import org.bson.types.ObjectId;

import com.model.CryptoCurrency;
import com.mongodb.DBObject;

public class Converter {
	// convert DBObject Object to Person
	// take special note of converting ObjectId to String
	public static CryptoCurrency CryptoCurrency(DBObject row) {
		CryptoCurrency currency = new CryptoCurrency();
		currency.setAvgPrice((int) row.get("price"));
		currency.setMarketPlace((String) row.get("market"));
		currency.setPair((String) row.get("pair"));
		currency.setDate((LocalDate) row.get("date"));
		return currency;

	}
}
