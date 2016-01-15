package ch.bfh.bti7321thesis.desc.cmd;

import java.util.ArrayList;
import java.util.List;

import ch.bfh.bti7321thesis.desc.cmd.Command;

public class CommandDescription {
	private final List<Command> commands = new ArrayList<Command>();

	public List<Command> getCommands() {
		return commands;
	}
	
	public boolean addCommand(Command command) {
		return this.commands.add(command);
	}

}
