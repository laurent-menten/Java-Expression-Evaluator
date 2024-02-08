/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.node;

import be.lmenten.math.evaluator.grammar.analysis.*;

@SuppressWarnings("nls")
public final class TInteger10 extends Token
{
    public TInteger10(String text)
    {
        setText(text);
    }

    public TInteger10(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TInteger10(getText(), getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTInteger10(this);
    }
}