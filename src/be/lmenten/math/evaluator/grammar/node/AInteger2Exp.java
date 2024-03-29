/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.node;

import be.lmenten.math.evaluator.grammar.analysis.*;

@SuppressWarnings("nls")
public final class AInteger2Exp extends PExp
{
    private TInteger2 _integer2_;

    public AInteger2Exp()
    {
        // Constructor
    }

    public AInteger2Exp(
        @SuppressWarnings("hiding") TInteger2 _integer2_)
    {
        // Constructor
        setInteger2(_integer2_);

    }

    @Override
    public Object clone()
    {
        return new AInteger2Exp(
            cloneNode(this._integer2_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAInteger2Exp(this);
    }

    public TInteger2 getInteger2()
    {
        return this._integer2_;
    }

    public void setInteger2(TInteger2 node)
    {
        if(this._integer2_ != null)
        {
            this._integer2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._integer2_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._integer2_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._integer2_ == child)
        {
            this._integer2_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._integer2_ == oldChild)
        {
            setInteger2((TInteger2) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
