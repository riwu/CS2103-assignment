import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {

	private static final String FILE_NAME = "mytextfile.txt";
	
	private TextBuddy textBuddy = new TextBuddy(new String[]{FILE_NAME});;
	ByteArrayOutputStream outContent;
	
	@Before
	public void setOutToStream() {
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}
	
	private void testCommand(String input, String expected) {
		outContent.reset();
		textBuddy.executeCommand(input);
		assertEquals(expected + "\n", outContent.toString());
	}
	
	private void testFile(String expected) {
		Scanner scanner = null;
		String content = "";
		try {
			scanner = new Scanner(new File(FILE_NAME));
			while (scanner.hasNextLine()) {
				content += scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		assertEquals(expected, content);
	}
	
	@Test
	public void testAdd() {	
		testCommand("clear", "all content deleted from mytextfile.txt");
		
		testCommand("add little brown fox", "added to mytextfile.txt: \"little brown fox\"");
		testCommand("display", "1. little brown fox");
		testFile("little brown fox\n");
		
		testCommand("add ", "No arguments entered");
		
		testCommand("add jumped over the moon", "added to mytextfile.txt: \"jumped over the moon\"");
		testCommand("display", "1. little brown fox\n2. jumped over the moon");
		testFile("little brown fox\njumped over the moon\n");
	}
	
	@Test
	public void testDelete() {
		testCommand("clear", "all content deleted from mytextfile.txt");
		
		testCommand("add little brown fox", "added to mytextfile.txt: \"little brown fox\"");
		testCommand("add jumped over the moon", "added to mytextfile.txt: \"jumped over the moon\"");

		testCommand("delete 2", "deleted from mytextfile.txt: \"jumped over the moon\"");
		
		testCommand("delete a", "Invalid index");
		testCommand("delete 2", "Invalid index");
		testCommand("delete", "No arguments entered");
		
		testCommand("display", "1. little brown fox");
		testFile("little brown fox\n");
	}
	
	@Test
	public void testClear() {
		testCommand("add little brown fox", "added to mytextfile.txt: \"little brown fox\"");

		testCommand("clear", "all content deleted from mytextfile.txt");
		testCommand("display", "mytextfile.txt is empty");
		testFile("");
	}
}
