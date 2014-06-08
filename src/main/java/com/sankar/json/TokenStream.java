package com.sankar.json;


public interface TokenStream {
	Token nextToken();
	void close();
}
