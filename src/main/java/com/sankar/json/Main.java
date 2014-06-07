package com.sankar.json;

public class Main 
{
    public static void main( String[] args )
    {
    	String input = "[[\"sank\\nar\"],[[{\"\":null} ]],{},{ \"one\":[ ],\"two\":-12.011123e3},null,true,false      ]";
    	
    	System.out.println(Parser.parse(input).toJson());
    }
}
