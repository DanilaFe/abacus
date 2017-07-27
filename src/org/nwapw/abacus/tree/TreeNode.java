package org.nwapw.abacus.tree;

import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;

import java.util.*;

/**
 * An abstract class that represents an expression tree node.
 */
public abstract class TreeNode {

    /**
     * The lexer used to lex tokens.
     */
    protected static Lexer<TokenType> lexer = new Lexer<TokenType>(){{
        register(",", TokenType.COMMA);
        register("\\+|-|\\*|/", TokenType.OP);
        register("[0-9]+(\\.[0-9]+)?", TokenType.NUM);
        register("[a-zA-Z]+", TokenType.WORD);
        register("\\(", TokenType.OPEN_PARENTH);
        register("\\)", TokenType.CLOSE_PARENTH);
    }};
    /**
     * A map that maps operations to their precedence.
     */
    protected static HashMap<String, Integer> precedenceMap = new HashMap<String, Integer>(){{
        put("+", 0);
        put("-", 0);
        put("*", 1);
        put("/", 1);
    }};
    /**
     * A map that maps operations to their associativity.
     */
    protected static HashMap<String, OperatorAssociativity> associativityMap =
            new HashMap<String, OperatorAssociativity>() {{
                put("+", OperatorAssociativity.LEFT);
                put("-", OperatorAssociativity.LEFT);
                put("*", OperatorAssociativity.LEFT);
                put("/", OperatorAssociativity.LEFT);
            }};

    /**
     * Comparator used to sort token types.
     */
    protected static Comparator<TokenType> tokenSorter = Comparator.comparingInt(e -> e.priority);

    /**
     * Tokenizes a string, converting it into matches
     * @param string the string to tokenize.
     * @return the list of tokens produced.
     */
    public static ArrayList<Match<TokenType>> tokenize(String string){
        return lexer.lexAll(string, 0, tokenSorter);
    }

    /**
     * Rearranges tokens into a postfix list, using Shunting Yard.
     * @param source the source string.
     * @param from the tokens to be rearranged.
     * @return the resulting list of rearranged tokens.
     */
    public static ArrayList<Match<TokenType>> intoPostfix(String source, ArrayList<Match<TokenType>> from){
        ArrayList<Match<TokenType>> output = new ArrayList<>();
        Stack<Match<TokenType>> tokenStack = new Stack<>();
        while(!from.isEmpty()){
            Match<TokenType> match = from.remove(0);
            TokenType matchType = match.getType();
            if(matchType == TokenType.NUM || matchType == TokenType.WORD) {
                output.add(match);
            } else if(matchType == TokenType.OP){
                String tokenString = source.substring(match.getFrom(), match.getTo());
                int precedence = precedenceMap.get(tokenString);
                OperatorAssociativity associativity = associativityMap.get(tokenString);

                while(!tokenStack.empty()) {
                    Match<TokenType> otherMatch = tokenStack.peek();
                    if(otherMatch.getType() != TokenType.OP) break;

                    int otherPrecdence = precedenceMap.get(source.substring(otherMatch.getFrom(), otherMatch.getTo()));
                    if(otherPrecdence < precedence ||
                            (associativity == OperatorAssociativity.RIGHT && otherPrecdence == precedence)) {
                        break;
                    }
                    output.add(tokenStack.pop());
                }
                tokenStack.push(match);
            } else if(matchType == TokenType.OPEN_PARENTH){
                if(!output.isEmpty() && output.get(output.size() - 1).getType() == TokenType.WORD){
                    tokenStack.push(output.remove(output.size() - 1));
                    output.add(new Match<>(0, 0, TokenType.INTERNAL_FUNCTION_END));
                }
                tokenStack.push(match);
            } else if(matchType == TokenType.CLOSE_PARENTH || matchType == TokenType.COMMA){
                while(!tokenStack.empty() && tokenStack.peek().getType() != TokenType.OPEN_PARENTH){
                    output.add(tokenStack.pop());
                }
                if(tokenStack.empty()) return null;
                if(matchType == TokenType.CLOSE_PARENTH){
                    tokenStack.pop();
                    if(!tokenStack.empty() && tokenStack.peek().getType() == TokenType.WORD) {
                        output.add(tokenStack.pop());
                        output.add(new Match<>(0, 0, TokenType.INTERNAL_FUNCTION_START));
                    }
                }
            }
        }
        while(!tokenStack.empty()){
            if(!(tokenStack.peek().getType() == TokenType.OP)) return null;
            output.add(tokenStack.pop());
        }
        return output;
    }

    /**
     * Constructs a tree recursively from a list of tokens.
     * @param source the source string.
     * @param matches the list of tokens from the source string.
     * @return the construct tree expression.
     */
    public static TreeNode fromStringRecursive(String source, ArrayList<Match<TokenType>> matches){
        if(matches.size() == 0) return null;
        Match<TokenType> match = matches.remove(0);
        TokenType matchType = match.getType();
        if(matchType == TokenType.OP){
            TreeNode right = fromStringRecursive(source, matches);
            TreeNode left = fromStringRecursive(source, matches);
            if(left == null || right == null) return null;
            else return new OpNode(source.substring(match.getFrom(), match.getTo()), left, right);
        } else if(matchType == TokenType.NUM){
            return new NumberNode(Double.parseDouble(source.substring(match.getFrom(), match.getTo())));
        } else if(matchType == TokenType.INTERNAL_FUNCTION_START){
            if(matches.isEmpty() || matches.get(0).getType() != TokenType.WORD) return null;
            Match<TokenType> stringName = matches.remove(0);
            String functionName = source.substring(stringName.getFrom(), stringName.getTo());
            FunctionNode node = new FunctionNode(functionName);
            while(!matches.isEmpty() && matches.get(0).getType() != TokenType.INTERNAL_FUNCTION_END){
                TreeNode argument = fromStringRecursive(source, matches);
                if(argument == null) return null;
                node.addChild(argument);
            }
            if(matches.isEmpty()) return null;
            matches.remove(0);
            return node;
        }
        return null;
    }

    /**
     * Creates a tree node from a string.
     * @param string the string to create a node from.
     * @return the resulting tree.
     */
    public static TreeNode fromString(String string){
        ArrayList<Match<TokenType>> matches = tokenize(string);
        if(matches == null) return null;
        matches = intoPostfix(string, matches);
        if(matches == null) return null;

        Collections.reverse(matches);
        return fromStringRecursive(string, matches);
    }

    public abstract <T> T reduce(Reducer<T> reducer);

}
