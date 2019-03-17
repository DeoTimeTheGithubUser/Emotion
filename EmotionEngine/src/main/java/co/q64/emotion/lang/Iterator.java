package co.q64.emotion.lang;

import java.util.LinkedList;
import java.util.Queue;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import co.q64.emotion.factory.LiteralFactoryFactory;
import co.q64.emotion.lang.value.LiteralFactory;
import co.q64.emotion.lang.value.Value;

@AutoFactory
public class Iterator {
	private Queue<Value> values;
	private Registers registers;
	private LiteralFactory literal;
	private Program program;
	private boolean onStack;
	private int index;
	private Value o, i;

	protected Iterator(@Provided LiteralFactoryFactory literal, Program program, boolean onStack) {
		this.program = program;
		this.registers = program.getRegisters();
		this.onStack = onStack;
		this.literal = literal.getFactory();
		this.index = 0;
		this.values = new LinkedList<>(program.getStack().pop().iterate());
	}

	public boolean next() {
		if (values.size() > 0) {
			i = literal.create(index);
			o = values.poll();
			register();
			if (onStack) {
				program.getStack().push(registers.getO());
			}
			index++;
			//program.jumpToNode(line);
			return false;
		}
		return true;
	}

	public void register() {
		registers.setI(i);
		registers.setO(o);
	}
}
