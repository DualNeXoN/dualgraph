package sk.dualnexon.dualgraph.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import javafx.scene.control.Alert.AlertType;
import sk.dualnexon.dualgraph.App;
import sk.dualnexon.dualgraph.GlobalSettings;

public class Logger {
	
	private static final String LOG_MESSAGE_FORMAT = "[%s] %s";
	private static LinkedList<String> logs = new LinkedList<>();
	
	public static void log(String message) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		String formattedMessage = String.format(LOG_MESSAGE_FORMAT, timeFormat.format(timestamp), message);
		logs.addLast(formattedMessage);
		if(GlobalSettings.logConsole()) System.out.println(message);
	}
	
	public static void saveLogs() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		
		File folderLogs = new File(System.getProperty("user.dir") + File.separator + "logs");
		if(!folderLogs.exists()) {
			folderLogs.mkdirs();
		}
		
		File logFile = new File(System.getProperty("user.dir") + File.separator + "logs" + File.separator + timeFormat.format(timestamp) + ".txt");
		if(!logFile.exists()) {
			try {
				logFile.createNewFile();
				BufferedWriter br = new BufferedWriter(new FileWriter(logFile));
				for(String log : logs) {
					br.write(log);
					br.newLine();
				}
				br.close();
				App.get().showAlert(AlertType.INFORMATION, "Logs saved");
			} catch (IOException e) {
				App.get().showAlert(AlertType.ERROR, "An unexpected error occurred while saving logs.\n" + e.getMessage());
			}
		}
	}
	
	public static void clearLogs() {
		logs.clear();
	}

}
