package com.criminal_code.calculator_1

enum class Symbols(val symbol: String) {

    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    EXP("2.71"),

    INIT(""),

    ADDITION(" + "),
    SUBTRACTION(" − "),
    MULTIPLICATION(" × "),
    DIVISION(" ÷ "),

    FRACTION("1/"),
    PERCENTAGE("%"),
    SQUARE("²"),
    ROOT("√"),
    CUBE("³"),
    DEGREE("^"),
    TENDEGREE("10^"),
    ROOTCUBE("3√"),
    ROOTN("√"),
    FACTORIAL("!"),
    EXPONENT("e"),
    EXPONENTDEGN("e^"),
    LN("ln"),
    LOG("log"),

    NEGATE("negate"),
    COMMA(","),
    BRACKET1("("),
    BRACKET2(")"),
    EQUAL(" = ")
}