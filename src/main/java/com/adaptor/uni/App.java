package com.adaptor.uni;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adaptor.uni.db.TUniversityDBManager;
import com.adaptor.uni.model.IUniversity;
import com.adaptor.uni.parser.IDepartmentParser;
import com.adaptor.uni.parser.IUniversityParser;
import com.adaptor.uni.parser.bogazici.TBogaziciDepartmentParser;
import com.adaptor.uni.parser.bogazici.TBogaziciUniversityParser;
import com.adaptor.uni.parser.odtu.TOdtuUniversityParser;
import com.adaptor.uni.utils.TAdaptorConstants;

/**
 * Hello world!
 *
 */
public class App 
{
	/**
	 * Logger for logging  events
	 * */
	public static  final Logger logger = LogManager.getLogger(App.class);
	/**
	 * Stores list of parsers, these parsers parse interested 
	 * school web sites periodically.
	 * */
	public List<IUniversityParser> parsers;
	
	/**
	 * Periodically fire parsers for parsing.
	 * */
	public Timer parseTimer;
	
    public static void main( String[] args )
    {
        App app = new App();
        app.initialize();
    }

	private void initialize() {
		initializeParsers();
		startParseTimer();
	}

	private void startParseTimer() {
		parseTimer = new Timer();
		parseTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				for (IUniversityParser parser : parsers) {
					IUniversity university = parser.parse();
					TUniversityDBManager manager = new TUniversityDBManager();
					manager.save(university);
				}
			}
		}, 0,TAdaptorConstants.PARSE_PERIOD);
	}

	private void initializeParsers() {
		parsers = new ArrayList<IUniversityParser>();
//		parsers.add(new TBogaziciUniversityParser());
		parsers.add(new TOdtuUniversityParser());
	}
}
