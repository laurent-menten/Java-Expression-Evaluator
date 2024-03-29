/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.analysis;

import java.util.*;
import be.lmenten.math.evaluator.grammar.node.*;

public class AnalysisAdapter implements Analysis
{
    private Hashtable<Node,Object> in;
    private Hashtable<Node,Object> out;

    @Override
    public Object getIn(Node node)
    {
        if(this.in == null)
        {
            return null;
        }

        return this.in.get(node);
    }

    @Override
    public void setIn(Node node, Object o)
    {
        if(this.in == null)
        {
            this.in = new Hashtable<Node,Object>(1);
        }

        if(o != null)
        {
            this.in.put(node, o);
        }
        else
        {
            this.in.remove(node);
        }
    }

    @Override
    public Object getOut(Node node)
    {
        if(this.out == null)
        {
            return null;
        }

        return this.out.get(node);
    }

    @Override
    public void setOut(Node node, Object o)
    {
        if(this.out == null)
        {
            this.out = new Hashtable<Node,Object>(1);
        }

        if(o != null)
        {
            this.out.put(node, o);
        }
        else
        {
            this.out.remove(node);
        }
    }

    @Override
    public void caseStart(Start node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAAssignVarExp(AAssignVarExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAAssignObjExp(AAssignObjExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAAddExp(AAddExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseASubExp(ASubExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAMulExp(AMulExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseADivExp(ADivExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAModExp(AModExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAEqualExp(AEqualExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseANotEqualExp(ANotEqualExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseALThanExp(ALThanExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseALEqualExp(ALEqualExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAGThanExp(AGThanExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAGEqualExp(AGEqualExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAAndExp(AAndExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAOrExp(AOrExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAXorExp(AXorExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAGetterExp(AGetterExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAFunc0Exp(AFunc0Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAFunc1Exp(AFunc1Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAFunc2Exp(AFunc2Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAFunc3Exp(AFunc3Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAInteger2Exp(AInteger2Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAInteger8Exp(AInteger8Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAInteger10Exp(AInteger10Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAInteger16Exp(AInteger16Exp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseADiceExp(ADiceExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAFloatExp(AFloatExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAFractionExp(AFractionExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAValueExp(AValueExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseAParenExp(AParenExp node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTBlank(TBlank node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpMultiply(TOpMultiply node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpDivide(TOpDivide node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpModulo(TOpModulo node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpPlus(TOpPlus node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpMinus(TOpMinus node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpEqual(TOpEqual node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpNotEqual(TOpNotEqual node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpLThan(TOpLThan node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpLEqual(TOpLEqual node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpGThan(TOpGThan node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpGEqual(TOpGEqual node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpAnd(TOpAnd node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpOr(TOpOr node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpXor(TOpXor node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTOpAssign(TOpAssign node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTLPar(TLPar node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTRPar(TRPar node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTComma(TComma node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTInteger2(TInteger2 node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTInteger8(TInteger8 node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTInteger10(TInteger10 node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTInteger16(TInteger16 node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTDice(TDice node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTFloat(TFloat node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTFraction(TFraction node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTIdentifier(TIdentifier node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTObjIdentifier(TObjIdentifier node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTConst(TConst node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTVar(TVar node)
    {
        defaultCase(node);
    }

    @Override
    public void caseTDelete(TDelete node)
    {
        defaultCase(node);
    }

    @Override
    public void caseEOF(EOF node)
    {
        defaultCase(node);
    }

    @Override
    public void caseInvalidToken(InvalidToken node)
    {
        defaultCase(node);
    }

    public void defaultCase(@SuppressWarnings("unused") Node node)
    {
        // do nothing
    }
}
