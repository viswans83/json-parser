package com.sankar.json;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
	
    public static void main(String[] args) throws IOException {
    	System.out.println(
			PrettyPrinter.toJson(
				new Parser(
					new Tokenizer(
						new InputStreamReader(System.in)
					)
				).parse()
			)
		);
    }
    
    static String readFile(String path, Charset encoding) throws IOException {
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
    
}
