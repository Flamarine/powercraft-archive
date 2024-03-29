/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package powercraft.weasel.jep.function;


import java.util.Stack;

import powercraft.weasel.jep.ParseException;
import powercraft.weasel.jep.type.Complex;



public class Abs extends PostfixMathCommand {
	public Abs() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(abs(param));//push the result on the inStack
		return;
	}


	public Object abs(Object param) throws ParseException {
		if (param instanceof Complex) {
			return new Double(((Complex) param).abs());
		} else if (param instanceof Number) {
			return new Double(Math.abs(((Number) param).doubleValue()));
		}

		throw new ParseException("abs() not defined for " + param.getClass().getSimpleName());
	}

}
