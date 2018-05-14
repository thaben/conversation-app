package com.mypackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class FileAccess { 

	public static void fileWriterAccess(Conversation c, Message m) {
		File directory = new File("conversation_logs");
		if (!directory.exists()) {
			directory.mkdir();
			// use directory.mkdirs(); if more folders needed
		}
		try {
			FileWriter fw = new FileWriter(directory.getPath() + "/" + c.getId() + "_" + ".txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(m.toString());
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printAdminAction(String text) {
		File directory = new File("users_actions");
		if (!directory.exists()) {
			directory.mkdir();
			// use directory.mkdirs(); if more folders needed
		}
		try {
			FileWriter fw = new FileWriter("users_actions/" + "admin_logs.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

