/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.analysis;

import be.lmenten.math.evaluator.grammar.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

    void caseStart(Start node);
    void caseAAssignVarExp(AAssignVarExp node);
    void caseAAssignObjExp(AAssignObjExp node);
    void caseAAddExp(AAddExp node);
    void caseASubExp(ASubExp node);
    void caseAMulExp(AMulExp node);
    void caseADivExp(ADivExp node);
    void caseAModExp(AModExp node);
    void caseAEqualExp(AEqualExp node);
    void caseANotEqualExp(ANotEqualExp node);
    void caseALThanExp(ALThanExp node);
    void caseALEqualExp(ALEqualExp node);
    void caseAGThanExp(AGThanExp node);
    void caseAGEqualExp(AGEqualExp node);
    void caseAAndExp(AAndExp node);
    void caseAOrExp(AOrExp node);
    void caseAXorExp(AXorExp node);
    void caseAGetterExp(AGetterExp node);
    void caseAFunc0Exp(AFunc0Exp node);
    void caseAFunc1Exp(AFunc1Exp node);
    void caseAFunc2Exp(AFunc2Exp node);
    void caseAFunc3Exp(AFunc3Exp node);
    void caseAInteger2Exp(AInteger2Exp node);
    void caseAInteger8Exp(AInteger8Exp node);
    void caseAInteger10Exp(AInteger10Exp node);
    void caseAInteger16Exp(AInteger16Exp node);
    void caseADiceExp(ADiceExp node);
    void caseAFloatExp(AFloatExp node);
    void caseAFractionExp(AFractionExp node);
    void caseAValueExp(AValueExp node);
    void caseAParenExp(AParenExp node);

    void caseTBlank(TBlank node);
    void caseTOpMultiply(TOpMultiply node);
    void caseTOpDivide(TOpDivide node);
    void caseTOpModulo(TOpModulo node);
    void caseTOpPlus(TOpPlus node);
    void caseTOpMinus(TOpMinus node);
    void caseTOpEqual(TOpEqual node);
    void caseTOpNotEqual(TOpNotEqual node);
    void caseTOpLThan(TOpLThan node);
    void caseTOpLEqual(TOpLEqual node);
    void caseTOpGThan(TOpGThan node);
    void caseTOpGEqual(TOpGEqual node);
    void caseTOpAnd(TOpAnd node);
    void caseTOpOr(TOpOr node);
    void caseTOpXor(TOpXor node);
    void caseTOpAssign(TOpAssign node);
    void caseTLPar(TLPar node);
    void caseTRPar(TRPar node);
    void caseTComma(TComma node);
    void caseTInteger2(TInteger2 node);
    void caseTInteger8(TInteger8 node);
    void caseTInteger10(TInteger10 node);
    void caseTInteger16(TInteger16 node);
    void caseTDice(TDice node);
    void caseTFloat(TFloat node);
    void caseTFraction(TFraction node);
    void caseTIdentifier(TIdentifier node);
    void caseTObjIdentifier(TObjIdentifier node);
    void caseTConst(TConst node);
    void caseTVar(TVar node);
    void caseTDelete(TDelete node);
    void caseEOF(EOF node);
    void caseInvalidToken(InvalidToken node);
}
