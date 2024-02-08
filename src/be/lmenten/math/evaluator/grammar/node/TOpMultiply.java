/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.node;

import be.lmenten.math.evaluator.grammar.analysis.*;

@SuppressWarnings("nls")
public final class TOpMultiply extends Token
{
    public TOpMultiply()
    {
        super.setText("*");
    }

    public TOpMultiply(int line, int pos)
    {
        super.setText("*");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TOpMultiply(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTOpMultiply(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TOpMultiply text.");
    }
}