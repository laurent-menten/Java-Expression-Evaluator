/* This file was generated by SableCC (http://www.sablecc.org/). */

package be.lmenten.math.evaluator.grammar.parser;

import be.lmenten.math.evaluator.grammar.node.*;

@SuppressWarnings("serial")
public class ParserException extends Exception
{
    private Token token;

    public ParserException(@SuppressWarnings("hiding") Token token, String  message)
    {
        super(message);
        this.token = token;
    }

    public Token getToken()
    {
        return this.token;
    }
}
