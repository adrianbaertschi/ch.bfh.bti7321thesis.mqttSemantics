package ch.bfh.bti7321thesis.tinkerforge.log;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;

public class StdoutConsoleHandler extends ConsoleHandler {
	  @Override
	protected void setOutputStream(OutputStream out) throws SecurityException {
	    super.setOutputStream(System.out); // kitten killed here :-(
	  }
	}