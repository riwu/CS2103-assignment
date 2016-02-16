
/**
 * This class manipulates text in a file. 
 * Extra command: save -- saves to content to file.
 * 
 * Assumptions: 
 * File passed to program must be readable and writable.
 * Existing text will be retrieved from file at start.
 * Will attempt to create file if does not exist initially.
 * Order of string is based on order in which it is stored.
 * Text will be written to file only upon "save" or "exit" command.
 * 
 * @author WANG RIWU
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TextBuddy {

	private static final String MESSAGE_PHRASE_DISPLAY = "%1$s. %2$s\n";
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use\n";
	private static final String MESSAGE_NO_ARGUMENTS = "No arguments entered\n";

	private static final String MESSAGE_ENTER_COMMAND = "command: ";

	private static final String MESSAGE_PHRASE_ADDED = "added to %1$s: \"%2$s\"\n";
	private static final String MESSAGE_PHRASE_DELETED = "deleted from %1$s: \"%2$s\"\n";
	private static final String MESSAGE_CONTENT_CLEARED = "all content deleted from %1$s\n";
	private static final String MESSAGE_CONTENT_EMPTY = "%1$s is empty\n";
	private static final String MESSAGE_SAVED_TO_FILE = "Successfully saved to: %1$s\n";

	private static final String MESSAGE_INVALID_INDEX = "Invalid index\n";
	private static final String MESSAGE_INVALID_COMMAND = "Invalid command: %1$s\n";

	private static final String COMMAND_EXIT = "exit";
	private static final String COMMAND_SAVE = "save";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_ADD = "add";

	private static final int LIST_NUMBERING_OFFSET = 1;

	private List<String> _phrases = new LinkedList<String>();
	private String _fileName;
	private Scanner _scanner = new Scanner(System.in);

	public static void main(String[] args) {
		TextBuddy textBuddy = new TextBuddy(args);
		textBuddy.executeCommandsUntilExitCommand();
	}

	/**
	 * Check for args, set variables and print welcome message
	 *
	 * @param args    args passed to the program
	 */
	public TextBuddy(String[] args) {
		exitIfNoArguments(args);
		setFileName(args);
		if (existsFile()) {
			getPhrasesFromFile();
		}
		printWelcomeMessage();
	}

	private void exitIfNoArguments(String[] args) {
		if (args.length == 0) {
			stopWithErrorMessage(MESSAGE_NO_ARGUMENTS);
		}
	}

	private void setFileName(String[] args) {
		_fileName = args[0];
	}

	private boolean existsFile() {
		File file = new File(_fileName);
		return file.isFile();
	}

	private void getPhrasesFromFile() {
		Path path = Paths.get(_fileName);
		try {
			_phrases = Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void showToUser(String message) {
		System.out.print(message);
	}

	private static void stopWithErrorMessage(String message) {
		showToUser(message);
		System.exit(1);
	}

	private void printWelcomeMessage() {
		showToUser(String.format(MESSAGE_WELCOME, _fileName));
	}

	public void executeCommandsUntilExitCommand() {
		while (true) {
			String userCommand = getNextCommand();
			executeCommand(userCommand);
		}
	}

	private String getNextCommand() {
		showToUser(MESSAGE_ENTER_COMMAND);
		return _scanner.next();

	}

	private void executeCommand(String command) {
		switch (command.toLowerCase()) {
			case COMMAND_ADD :
				addPhrase();
				break;

			case COMMAND_DISPLAY :
				displayPhrases();
				break;

			case COMMAND_DELETE :
				deletePhrase();
				break;

			case COMMAND_CLEAR :
				clearItems();
				break;

			case COMMAND_SAVE :
				saveToFileWithFeedback();
				break;

			case COMMAND_EXIT :
				saveToFileAndExit();
				break;

			default :
				printInvalidCommand(command);
		}
	}

	private void addPhrase() {
		String phrase = _scanner.nextLine().trim();
		_phrases.add(phrase);
		showToUser(String.format(MESSAGE_PHRASE_ADDED, _fileName, phrase));
	}

	private void displayPhrases() {
		if (_phrases.isEmpty()) {
			showToUser(String.format(MESSAGE_CONTENT_EMPTY, _fileName));
			return;
		}
		for (int i = 0; i < _phrases.size(); i++) {
			showToUser(String.format(MESSAGE_PHRASE_DISPLAY, i + LIST_NUMBERING_OFFSET, _phrases.get(i)));
		}
	}

	private void deletePhrase() {
		int index = getIndex();
		deletePhraseAtIndex(index);
	}

	/**
	 * Get index to be removed from list from user input
	 * 
	 * @return    index of list element to be removed, -1 if invalid index
	 */
	private int getIndex() {
		int index = getUserInputIndex();
		return (isIndexValid(index)) ? (index - LIST_NUMBERING_OFFSET) : -1;
	}

	private int getUserInputIndex() {
		String indexStr = _scanner.next();
		return (indexStr.matches("\\d")) ? Integer.parseInt(indexStr) : -1;
	}

	private boolean isIndexValid(int index) {
		if (index > _phrases.size() || index <= 0) {
			showToUser(MESSAGE_INVALID_INDEX);
			return false;
		}
		return true;
	}

	private void deletePhraseAtIndex(int index) {
		if (index == -1) {
			return;
		}
		String deletedPhrase = _phrases.remove(index);
		showToUser(String.format(MESSAGE_PHRASE_DELETED, _fileName, deletedPhrase));
	}

	private void clearItems() {
		_phrases.clear();
		showToUser(String.format(MESSAGE_CONTENT_CLEARED, _fileName));
	}

	private String getCombinedPhrases() {
		String combinedPhrases = "";
		for (String phrase : _phrases) {
			combinedPhrases += phrase + "\n";
		}
		return combinedPhrases;
	}

	private boolean writeToFile(String combinedPhrases) {
		FileWriter file = null;
		try {
			file = new FileWriter(_fileName);
			file.write(combinedPhrases);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean saveToFile() {
		String combinedPhrases = getCombinedPhrases();
		return writeToFile(combinedPhrases);
	}

	private void saveToFileWithFeedback() {
		if (saveToFile()) {
			showToUser(String.format(MESSAGE_SAVED_TO_FILE, _fileName));
		}
	}

	private void saveToFileAndExit() {
		saveToFile();
		System.exit(0);
	}

	private void printInvalidCommand(String command) {
		showToUser(String.format(MESSAGE_INVALID_COMMAND, command));
	}
}
