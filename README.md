# Java Expression Evaluator

Java Expression Evaluator is simple mathematical expression evaluator for java.
It is lightweight and has no external dependencies.
Its grammar was developed using SableCC 3.7.

(c)2022+ Laurent Menten. Licensed under GPLv3.

# Expressions syntax

Java Expression Evaluator is a simple mathematical expression evaluator that
operates on strings. It does all its computations using double floating
points numbers.

```
ExpressionEvaluator evaluator = new ExpressionEvaluator();

String expression = "1+2*3-4";
double result = evaluator.evaluate( expression );
```

## Operators

- math: + - * / % ()
- logic: = <> < <= > >= & | ^
- assignation: ->

## Literals

- Binary with either the '0b' prefix or 'b' suffix.
- Octal with the '0' prefix.
- Decimal.
- Hexadecimal with '0x', '$', '#' prefix or 'h' suffix.
- Fractions with '#' separator for units, numerator and denominator (ex. 1#3#8 = 1 3/3).
- Floating point format as supported using the java _Double.parseDouble( String string )_ method.

All integer formats support the '_' cosmetic separator (ex. 12_3456_78 = 12345678 ).

## Dice Standard Notation

Dice with any number of faces.
Supports dropping or keeping an arbitrary number of the lowest or highest results.

ex:
- D12, 1D12, 2D12, roll one, one or two D12.
- 2d20-L or 2d20+H, roll two D20 and drop the lowest or keep the highest result.
- 3d20+2L or 3d20-2H, roll three D20 and drop the two highest or keep the two lowest result.

You can check if last expression had dice rolls with the _boolean hasDiceRolls();_ method.

You can access the dice rolls with a calls to the _List\<DiceRoll\> getRolls();_ method.

A DiceRoll object contains information about the set of single results of a dice roll and they
can be accessed with the following methods:
```
public int getFacesCount();
public int getRollsCount();
public int[] getRolls();
public int getKeptValuesCount();
public boolean[] getKeptValues();
public int getResult();
```

Note: you should add a space character after a dice literal to separate if from the following
mathematical expression: 2d20 +2

## Constants

Support for a set of builtin constants as well as user provided constants.
- true
- false
- NaN
- PositiveInfinity
- NegativeInfinity
- pi
- e

Note that constants names are case-sensitive.

## Variables

Support for a set of builtin variables as well as user provided variables.
- ans
- tmp

Automatic creation of variables is disabled by default but can be enabled
using _setAutoCreateVariablesEnabled( true )_.

Note that constants names are case-sensitive.

# Programmatic interface

## Constants

Constants are _**immutable**_ values that can be used by name in a mathematical
expression. The name is case-sensitive.

Constants can be added, removed or queried from an evaluator instance using
the following methods:
```
public void addConstant( Constant constant );
public void addConstants( Constant ... constants );

public void removeConstant( String name );
public void removeConstant( Constant constant );

public Constant getConstant( String constantName );
public List<String> getConstantsList();
```

To add a new constant to an evaluator, you can use the following typical example:
```
Constant myConstant = new Constant( "myConstant", 12.3456d );
evaluator.addConstant( myConstant );
```

## Variables

Variables are _**mutable**_ values that can be used by name in a mathematical
expression. The name is case-sensitive.

Variables can be added, removed or queried from an evaluator instance using
the following methods:
```
public void addVariable( Variable variable );
public void addVariables( Variable ... variables );

public void removeVariable( String name );
public void removeVariable( Variable variable );

public Variable getVariable( String variableName );
public List<String> getVariablesList();
```

You can either use an instance of the class Variable:
```
Variable myVariable1 = new Variable( "myVariable" ); // with default to 0.0d
Variable myVariable2 = new Variable( "myVariable2", 12.3456d ); // with the given default value
evaluator.addVariables( myVariable1, myVariable2 );
```

Or you can create a class derived from Variable and override the _Double getValue();_ and
_void setValue( Double value )_ if you need a finer control over the values. This way you can
create an accumulator or a Fibonacci series generator.

## Named objects

Named objects are a more sophisticated version of the variable. They provide access to internal properties
of an object using a dot notation.

Access to properties can be done in two ways, first if the object implements the _PropertyValueConsumer_
and _PropertyValueProvider_ method, the methods of these interfaces will be used or the access to the
_objectName.propertyName_ property is translated to a reflection call to the _Double getPropertyName()_
or _void setPropertyName( Double value )_ method of the given object instance.

Named objects can be added, removed or queried from an evaluator instance using
the following methods:
```
public void addNamedObject( NamedObject namedObject );
public void addNamedObjects( NamedObject ... namedObjects );

public void removeNamedObject( String objectName );
public void removeNamedObject( NamedObject namedObject );

public NamedObject getNamedObject( String objectName );
public List<String> getNamedObjectsList();
```

Named objects are usually added to an evaluator this way:
```
MyObject myObject = new MyObject();
NamedObject myNamedObject = new NamedObject( "myObjectName", myObject );
evaluator.addNamedObject( myNamedObject );
```

Suppose you have a property named _salary_ in the "myObject" named object of type MyObject.

- Read value of the "salary" property:
  - if object implements PropertyValueProvider, call to _getPropertyValue( "salary" )_.
  - otherwise a call via reflection to _Double getSalary()_.
- Write value of the "salary" property:
    - if object implements PropertyValueConsumer, call to _setPropertyValue( "salary", value )_.
    - otherwise a call via reflection to _Void setSalary( value )_.

Note: When using reflection, the property name is case-sensitive. When using the interfaces, case
sensitivity is handled by user code.

## Functions

Functions compute a value from their 0, 1, 2 or 3 parameters.

Note that you can overload a function with a difference parameter count
(see example below, all functions are created with the same name). The evaluator
will call the right function according to the number of passed parameters.

Functions can be added, removed or queried from an evaluator instance using
the following methods:
```
public void addFunction( Function function );
public void addFunctions( Function ... functions );

public void removeFunction( String functionName, Class<? extends Function> clazz );
public void removeFunction( Function function );

public Function getFunction( String functionName, Class<? extends Function> clazz );
public List<String> getFunctionsList();
```

You can either create a Function object:
```
Function myFunction0 = new Function0( "function", () -> 1.0d );
Function myFunction1 = new Function0( "function", (a) -> a );
Function myFunction2 = new Function0( "function", (a,b) -> a + b );
Function myFunction3 = new Function0( "function", (a,b,c) -> (a + b) * c );
evaluator.addFunctions( function0, function1, function2, function3 );
```

You can also create a derived class from Function0, Function1, Function2 or Function3
and override its _Double compute(...)_ method.
