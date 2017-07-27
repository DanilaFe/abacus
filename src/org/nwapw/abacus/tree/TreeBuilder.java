package org.nwapw.abacus.tree;

import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;
import org.nwapw.abacus.lexing.pattern.Pattern;

import java.util.*;

/**
 * The builder responsible for turning strings into trees.
 */
public class TreeBuilder {

    /**
     * The lexer used to get the input tokens.
     */
    private Lexer<TokenType> lexer;
    /**
     * The map of operator precedences.
     */
    private HashMap<String, Integer> precedenceMap;
    /**
     * The map of operator associativity.
     */
    private HashMap<String, OperatorAssociativity> associativityMap;

    /**
     * Comparator used to sort token types.
     */
    protected static Comparator<TokenType> tokenSorter = Comparator.comparingInt(e -> e.priority);

    /**
     * Creates a new TreeBuilder.
     */
    public TreeBuilder(){
        lexer = new Lexer<TokenType>(){{
            register(",", TokenType.COMMA);
            register("[0-9]+(\\.[0-9]+)?", TokenType.NUM);
            register("\\(", TokenType.OPEN_PARENTH);
            register("\\)", TokenType.CLOSE_PARENTH);
        }};
        precedenceMap = new HashMap<>();
        associativityMap = new HashMap<>();
    }

    /**
     * Registers a function with the TreeBuilder.
     * @param function the function to register.
     */
    public void registerFunction(String function){
        lexer.register(Pattern.sanitize(function), TokenType.FUNCTION);
    }

    /**
     * Registers an operator with the TreeBuilder.
     * @param operator the operator to register.
     * @param precedence the precedence of the operator.
     * @param associativity the associativity of the operator.
     */
    public void registerOperator(String operator, int precedence, OperatorAssociativity associativity){
        lexer.register(Pattern.sanitize(operator), TokenType.OP);
        precedenceMap.put(operator, precedence);
        associativityMap.put(operator, associativity);
    }

    /**
     * Tokenizes a string, converting it into matches
     * @param string the string to tokenize.
     * @return the list of tokens produced.
     */
    public ArrayList<Match<TokenType>> tokenize(String string){
        return lexer.lexAll(string, 0, tokenSorter);
    }

    /**
     * Rearranges tokens into a postfix list, using Shunting Yard.
     * @param source the source string.
     * @param from the tokens to be rearranged.
     * @return the resulting list of rearranged tokens.
     */
    public ArrayList<Match<TokenType>> intoPostfix(String source, ArrayList<Match<TokenType>> from){
        ArrayList<Match<TokenType>> output = new ArrayList<>();
        Stack<Match<TokenType>> tokenStack = new Stack<>();
        while(!from.isEmpty()){
            Match<TokenType> match = from.remove(0);
            TokenType matchType = match.getType();
            if(matchType == TokenType.NUM) {
                output.add(match);
            } else if(matchType == TokenType.FUNCTION) {
                output.add(new Match<>(0, 0, TokenType.INTERNAL_FUNCTION_END));
                tokenStack.push(match);
            } else if(matchType == TokenType.OP){
                String tokenString = source.substring(match.getFrom(), match.getTo());
                int precedence = precedenceMap.get(tokenString);
                OperatorAssociativity associativity = associativityMap.get(tokenString);

                while(!tokenStack.empty()) {
                    Match<TokenType> otherMatch = tokenStack.peek();
                    TokenType otherMatchType = otherMatch.getType();
                    if(otherMatchType != TokenType.OP) break;

                    int otherPrecdence = precedenceMap.get(source.substring(otherMatch.getFrom(), otherMatch.getTo()));
                    if(otherPrecdence < precedence ||
                            (associativity == OperatorAssociativity.RIGHT && otherPrecdence == precedence)) {
                        break;
                    }
                    output.add(tokenStack.pop());
                }
                tokenStack.push(match);
            } else if(matchType == TokenType.OPEN_PARENTH){
                tokenStack.push(match);
            } else if(matchType == TokenType.CLOSE_PARENTH || matchType == TokenType.COMMA){
                while(!tokenStack.empty() && tokenStack.peek().getType() != TokenType.OPEN_PARENTH){
                    output.add(tokenStack.pop());
                }
                if(tokenStack.empty()) return null;
                if(matchType == TokenType.CLOSE_PARENTH){
                    tokenStack.pop();
                }
            }
        }
        while(!tokenStack.empty()){
            Match<TokenType> match = tokenStack.peek();
            TokenType matchType = match.getType();
            if(!(matchType == TokenType.OP || matchType == TokenType.FUNCTION)) return null;
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
    public TreeNode fromStringRecursive(String source, ArrayList<Match<TokenType>> matches){
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
        } else if(matchType == TokenType.FUNCTION){
            String functionName = source.substring(match.getFrom(), match.getTo());
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
    public TreeNode fromString(String string){
        ArrayList<Match<TokenType>> matches = tokenize(string);
        if(matches == null) return null;
        matches = intoPostfix(string, matches);
        if(matches == null) return null;

        Collections.reverse(matches);
        return fromStringRecursive(string, matches);
    }

}
