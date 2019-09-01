// Whole purpose of this servlet setting mongoclinet as session parameter so that each servlet can access it for a given session.

package com.controller;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

@WebListener
public class ContextCheckerServlet implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Session destroyed");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext contextt= arg0.getServletContext();
		
		
		//try to connect mongodb and try finding a document to test if connection is fine.
		try {
			System.out.println("Context created");
			MongoClient database = new MongoClient("localhost");
			DB exchange_db = database.getDB("exchangedb_2");
			
			DBCollection collection = exchange_db.getCollection("exchange_deneme");

			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("market", "Dummy Market");
			DBCursor cursor = collection.find(searchQuery);
			 

			
			arg0.getServletContext().setAttribute("db_client", database);
		} catch (UnknownHostException e) {
			System.out.println("Failll");
			e.printStackTrace();
		}
		
	}

}
