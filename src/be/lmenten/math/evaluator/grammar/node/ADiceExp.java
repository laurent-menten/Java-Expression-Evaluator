/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.node;

import be.lmenten.math.evaluator.grammar.analysis.*;

@SuppressWarnings("nls")
public final class ADiceExp extends PExp
{
    private TDice _dice_;

    public ADiceExp()
    {
        // Constructor
    }

    public ADiceExp(
        @SuppressWarnings("hiding") TDice _dice_)
    {
        // Constructor
        setDice(_dice_);

    }

    @Override
    public Object clone()
    {
        return new ADiceExp(
            cloneNode(this._dice_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADiceExp(this);
    }

    public TDice getDice()
    {
        return this._dice_;
    }

    public void setDice(TDice node)
    {
        if(this._dice_ != null)
        {
            this._dice_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._dice_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._dice_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._dice_ == child)
        {
            this._dice_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._dice_ == oldChild)
        {
            setDice((TDice) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}