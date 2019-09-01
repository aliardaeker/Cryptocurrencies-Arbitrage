package com.model;
// represents char x axis
public class ChartData {
  public double percent;
 public String pair="";
 public String color="";
 public ChartData(String pair,double percent,String color){
	 this.pair=pair;
	 this.percent=percent;
	 this.color=color;
 }
}