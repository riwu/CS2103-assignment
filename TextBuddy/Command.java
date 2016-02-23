
public class Command {
	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_ADD = "add";

	public enum CommandType {
		ADD, DISPLAY, DELETE, CLEAR, EXIT, INVALID
	};

	private CommandType _commandType;
	private String _arguments;

	public Command(String input) {
		setCommandTypeAndArguments(input);
	}

	private void setCommandTypeAndArguments(String input) {
		String[] commandDetails = splitCommand(input);
		switch (commandDetails.length) {
			case 2 :
				SetArguments(commandDetails[1]);
				// Fallthrough

			case 1 :
				setCommandType(commandDetails[0]);
				break;
		}
	}

	private String[] splitCommand(String input) {
		return input.trim().split(" ", 2);
	}

	private void SetArguments(String arguments) {
		_arguments = arguments;
	}

	private void setCommandType(String commandTypeStr) {
		switch (commandTypeStr.toLowerCase()) {
			case COMMAND_ADD :
				_commandType = CommandType.ADD;
				break;

			case COMMAND_DISPLAY :
				_commandType = CommandType.DISPLAY;
				break;

			case COMMAND_DELETE :
				_commandType = CommandType.DELETE;
				break;

			case COMMAND_CLEAR :
				_commandType = CommandType.CLEAR;
				break;

			case COMMAND_EXIT :
				_commandType = CommandType.EXIT;
				break;

			default :
				_commandType = CommandType.INVALID;
		}
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public String getArguments() {
		return _arguments;
	}
}
