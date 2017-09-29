package jbse.algo.meta;

import static jbse.algo.Util.exitFromAlgorithm;
import static jbse.algo.Util.failExecution;
import static jbse.algo.Util.throwVerifyError;

import java.util.function.Supplier;

import jbse.algo.Algo_INVOKEMETA_Nonbranching;
import jbse.algo.InterruptException;
import jbse.mem.Instance_JAVA_CLASS;
import jbse.mem.State;
import jbse.mem.exc.ThreadStackEmptyException;
import jbse.val.Reference;
import jbse.val.Simplex;

/**
 * Meta-level implementation of {@link java.lang.Class#isPrimitive()}.
 * 
 * @author Pietro Braione
 */
public final class Algo_JAVA_CLASS_ISPRIMITIVE extends Algo_INVOKEMETA_Nonbranching {
	private Simplex isPrimitive; //set by cookMore
	
    @Override
    protected Supplier<Integer> numOperands() {
        return () -> 1;
    }
    
    @Override
    protected void cookMore(State state) throws InterruptException {
        try {
            final Instance_JAVA_CLASS thisObject = (Instance_JAVA_CLASS) state.getObject((Reference) this.data.operand(0));
            if (thisObject == null) {
                //this should never happen
                failExecution("violated invariant (unexpected heap access with symbolic unresolved reference)");
            }
            this.isPrimitive = state.getCalculator().valBoolean(thisObject.isPrimitive());
        } catch (ClassCastException e) {
            throwVerifyError(state);
            exitFromAlgorithm();
        }
    }

    @Override
    protected void update(State state) throws ThreadStackEmptyException {
        state.pushOperand(this.isPrimitive);
    }
}
