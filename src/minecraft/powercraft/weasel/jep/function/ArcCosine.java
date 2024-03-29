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



/**
 * The acos function.
 * 
 * @author Nathan Funk TODO How to handle acos(x) for real x with x>1 or x<-1
 */
public class ArcCosine extends PostfixMathCommand {
	public ArcCosine() {
		numberOfParameters = 1;

	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(acos(param));//push the result on the inStack
		return;
	}

	public Object acos(Object param) throws ParseException {
		if (param instanceof Complex) {
			return ((Complex) param).acos();
		} else if (param instanceof Number) {
			return new Double(Math.acos(((Number) param).doubleValue()));
		}

		throw new ParseException("asin() not defined for " + param.getClass().getSimpleName());
	}

}
