package com.sankar.json;


public interface TokenSource {
	Token nextToken();
	void close();
}
