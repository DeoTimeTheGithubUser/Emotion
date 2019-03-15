package co.q64.emotion.tea;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

import co.q64.emotion.compiler.CompilerOutput;
import co.q64.emotion.lang.Instruction;
import co.q64.emotion.lang.opcode.Chars;
import co.q64.emotion.tea.inject.DaggerEmotionComponent;
import co.q64.emotion.tea.inject.EmotionComponent;

public class EmotionTea {
	EmotionComponent component;

	public static void main(String[] args) {
		EmotionTea instance = new EmotionTea();
		instance.init();
	}

	private void init() {
		component = DaggerEmotionComponent.create();
		setFunctions(program -> {
			CompilerOutput co = component.getCompiler().compile(Arrays.asList(program.replaceAll("\\r", "").split("\n")));
			CompiledCode cc = createCompiledCode();
			cc.setOutput(co.getDisplayOutput().stream().collect(Collectors.joining("\n")));
			if (co.isSuccess()) {
				cc.setProgram(co.getProgram());
			}
			return cc;
		}, (program, args) -> {
			CompilerOutput compiled = component.getCompiler().compile(Arrays.asList(program.replaceAll("\\r", "").split("\n")));
			if (!compiled.isSuccess()) {
				return "Fatal: Could not run program due to compiler error!";
			}
			OutputBuffer buffer = new OutputBuffer();
			component.getEngine().runProgram(compiled.getProgram(), args, buffer);
			return buffer.toString();
		}, () -> component.getOpcodes().getNames().toArray(new String[0]), name -> component.getOpcodes().getDescription(name), program -> {
			OutputBuffer output = new OutputBuffer();
			for (Instruction insn : component.getLexer().parse(program, output)) {
				output.println(insn.getInstruction());
			}
			return output.toString();
		}, () -> {
			return Arrays.asList(Chars.values()).stream().map(Chars::getCharacter).collect(Collectors.toList()).toArray(new String[0]);
		});
	}

	@JSFunctor
	@FunctionalInterface
	private static interface Compile extends JSObject {
		public CompiledCode compile(String program);
	}

	@JSFunctor
	@FunctionalInterface
	private static interface Execute extends JSObject {
		public String execute(String program, String args);
	}

	@JSFunctor
	@FunctionalInterface
	private static interface Decompile extends JSObject {
		public String execute(String program);
	}

	@JSFunctor
	@FunctionalInterface
	private static interface GetOpcodes extends JSObject {
		public String[] getOpcodes();
	}

	@JSFunctor
	@FunctionalInterface
	private static interface GetOpcodeName extends JSObject {
		public String getOpcodeName(String opcode);
	}

	@JSFunctor
	@FunctionalInterface
	private static interface GetCodepage extends JSObject {
		public String[] getCodepage();
	}

	private static interface CompiledCode extends JSObject {
		public @JSProperty String getProgram();

		public @JSProperty void setProgram(String program);

		public @JSProperty String getOutput();

		public @JSProperty void setOutput(String output);
	}

	@JSBody(params = { "emotionCompiler", "emotionExecutor", "emotionGetOpcodes", "emotionGetOpcodeName", "emotionDecompile", "emotionGetCodepage" }, //
			script = "window.compile = emotionCompiler; window.execute = emotionExecutor; window.getOpcodes = emotionGetOpcodes; window.getOpcodeName = emotionGetOpcodeName; window.decompile = emotionDecompile; window.getCodepage = emotionGetCodepage;")
	private static native void setFunctions(Compile compile, Execute execute, GetOpcodes getOpcodes, GetOpcodeName getOpcodeName, Decompile decompile, GetCodepage codepage);

	@JSBody(script = "return {};")
	private static native CompiledCode createCompiledCode();
}
