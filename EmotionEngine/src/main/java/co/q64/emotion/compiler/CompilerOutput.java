package co.q64.emotion.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.emotion.annotation.Constants.Author;
import co.q64.emotion.annotation.Constants.Version;
import co.q64.emotion.lang.opcode.Opcodes;
import lombok.Getter;

@AutoFactory
public class CompilerOutput {
	private String author;
	private String version;
	private Opcodes opcodes;
	private @Getter boolean success;
	private @Getter String error;
	private List<String> compiledLines, instructionLines;

	protected CompilerOutput(String author, String version, Opcodes opcodes) {
		this.author = author;
		this.version = version;
		this.opcodes = opcodes;
	}

	protected CompilerOutput(@Provided @Author String author, @Provided @Version String version, @Provided Opcodes opcodes, String error) {
		this(author, version, opcodes);
		this.error = error;
		this.success = false;
	}

	protected CompilerOutput(@Provided @Author String author, @Provided @Version String version, @Provided Opcodes opcodes, List<String> compiledLines, List<String> instructionLines) {
		this(author, version, opcodes);
		this.compiledLines = compiledLines;
		this.instructionLines = instructionLines;
		this.success = true;
	}

	public List<String> getDisplayOutput() {
		List<String> result = new ArrayList<>();
		if (success) {
			int offsetLength = 0;
			for (String s : instructionLines) {
				if (s.length() > offsetLength) {
					offsetLength = s.length();
				}
			}
			result.add(getProgram());
			result.add(new String());
			result.add("Size: " + getProgram().codePointCount(0, getProgram().length()) + " bytes");
			result.add("Instructions: " + instructionLines.size());
			result.add(new String());
			for (int i = 0; i < compiledLines.size(); i++) {
				if (instructionLines.size() <= i) {
					continue;
				}
				String instruction = instructionLines.get(i);
				String offset = new String();
				for (int u = 0; u < offsetLength - instruction.length(); u++) {
					offset += " ";
				}
				String compiled = compiledLines.get(i);
				if (compiled.equals(" ")) {
					compiled = "<whitespace character>";
				}
				result.add((i + 1) + ": " + instruction + offset + " => " + compiled);
			}
			result.add(new String());
			offsetLength = 0;
			for (String s : compiledLines) {
				int points = s.codePointCount(0, s.length());
				if (points > offsetLength) {
					offsetLength = points;
				}
			}
			System.out.println("Line code points:");
			for (int i = 0; i < compiledLines.size(); i++) {
				if (instructionLines.size() <= i) {
					continue;
				}
				String instruction = instructionLines.get(i);
				String compiled = compiledLines.get(i);
				String offset = new String();
				String description = opcodes.getDescription(instruction);
				if (instruction.startsWith("load")) {
					description = "Push literal " + instruction.substring(5);
				}
				System.out.println(compiled.codePointCount(0, compiled.length()));
				//for (int u = 0; u < offsetLength - compiled.codePointCount(0, compiled.length()); u++) {
				//	offset += "  ";
				//}
				if (compiled.equals(" ")) {
					compiled = "<whitespace character>";
				}
				result.add(compiled + offset + " " + description);
			}
			//String program = getProgram();
			//if (program.length() % 2 == 1) {
			//	  program += opcodes.getChars(OpcodeMarker.EXIT).getCharacter();
			//}
			/*
			char[] chars = program.toCharArray();
			StringBuilder compressed = new StringBuilder();
			for (int i = 0; i < chars.length; i += 2) {
				int point = ((Chars.fromCode(String.valueOf(chars[i])).getByte() & 0xff) << 8) | (Chars.fromCode(String.valueOf(chars[i + 1])).getByte() & 0xff);
				compressed.append(insanity.getCharacter(point));
			}
			if (compressed.length() > 0) {
				result.add(new String());
				result.add("With insanity compression (" + chars.length / 2 + " bytes)");
				result.add(compressed.toString());
			}
			*/
		} else {
			result.add(error);
		}
		result.add(new String());
		result.add("Generated by the Harmony compiler version " + version + " by " + author + ".");
		return result;
	}

	public String getProgram() {
		return compiledLines.stream().collect(Collectors.joining());
	}
}
