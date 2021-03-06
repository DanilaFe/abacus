package org.nwapw.abacus.parsing.standard;

import org.nwapw.abacus.exception.ParseException;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.function.OperatorType;
import org.nwapw.abacus.lexing.Match;
import org.nwapw.abacus.parsing.Parser;
import org.nwapw.abacus.plugin.PluginListener;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.tree.nodes.*;

import java.util.*;

/**
 * A parser that uses shunting yard to rearranged matches into postfix
 * and then convert them into a parse tree.
 */
public class ShuntingYardParser implements Parser<Match<TokenType>>, PluginListener {

    /**
     * Map of operator precedences, loaded from the plugin operators.
     */
    private Map<String, Integer> precedenceMap;
    /**
     * Map of operator associativity, loaded from the plugin operators.
     */
    private Map<String, OperatorAssociativity> associativityMap;
    /**
     * Map of operator types, loaded from plugin operators.
     */
    private Map<String, OperatorType> typeMap;

    /**
     * Creates a new Shunting Yard parser.
     */
    public ShuntingYardParser() {
        precedenceMap = new HashMap<>();
        associativityMap = new HashMap<>();
        typeMap = new HashMap<>();
    }

    /**
     * Rearranges tokens into a postfix list, using Shunting Yard.
     *
     * @param from the tokens to be rearranged.
     * @return the resulting list of rearranged tokens.
     */
    public List<Match<TokenType>> intoPostfix(List<Match<TokenType>> from) {
        ArrayList<Match<TokenType>> output = new ArrayList<>();
        Stack<Match<TokenType>> tokenStack = new Stack<>();
        TokenType previousType;
        TokenType matchType = null;
        while (!from.isEmpty()) {
            Match<TokenType> match = from.remove(0);
            previousType = matchType;
            matchType = match.getType();
            if (matchType == TokenType.NUM || matchType == TokenType.VARIABLE) {
                output.add(match);
            } else if (matchType == TokenType.FUNCTION || matchType == TokenType.TREE_VALUE_FUNCTION) {
                output.add(new Match<>("", TokenType.INTERNAL_FUNCTION_END));
                tokenStack.push(match);
            } else if (matchType == TokenType.OP || matchType == TokenType.TREE_VALUE_OP) {
                String tokenString = match.getContent();
                OperatorType type = typeMap.get(tokenString);
                int precedence = precedenceMap.get(tokenString);
                OperatorAssociativity associativity = associativityMap.get(tokenString);

                if (type == OperatorType.UNARY_POSTFIX) {
                    output.add(match);
                    continue;
                }

                if (tokenString.equals("-") && (previousType == null || previousType == TokenType.OP ||
                        previousType == TokenType.TREE_VALUE_OP || previousType == TokenType.OPEN_PARENTH)) {
                    from.add(0, new Match<>("`", TokenType.OP));
                    continue;
                }

                while (!tokenStack.empty() && type == OperatorType.BINARY_INFIX) {
                    Match<TokenType> otherMatch = tokenStack.peek();
                    TokenType otherMatchType = otherMatch.getType();
                    if (!(otherMatchType == TokenType.OP ||
                            otherMatchType == TokenType.TREE_VALUE_OP ||
                            otherMatchType == TokenType.FUNCTION ||
                            otherMatchType == TokenType.TREE_VALUE_FUNCTION)) break;

                    if (otherMatchType == TokenType.OP || otherMatchType == TokenType.TREE_VALUE_OP) {
                        int otherPrecedence = precedenceMap.get(otherMatch.getContent());
                        if (otherPrecedence < precedence ||
                                (associativity == OperatorAssociativity.RIGHT && otherPrecedence == precedence)) {
                            break;
                        }
                    }
                    output.add(tokenStack.pop());
                }
                tokenStack.push(match);
            } else if (matchType == TokenType.OPEN_PARENTH) {
                tokenStack.push(match);
            } else if (matchType == TokenType.CLOSE_PARENTH || matchType == TokenType.COMMA) {
                while (!tokenStack.empty() && tokenStack.peek().getType() != TokenType.OPEN_PARENTH) {
                    output.add(tokenStack.pop());
                }
                if (tokenStack.empty()) throw new ParseException("mismatched parentheses");
                if (matchType == TokenType.CLOSE_PARENTH) {
                    tokenStack.pop();
                }
            }
        }
        while (!tokenStack.empty()) {
            Match<TokenType> match = tokenStack.peek();
            TokenType newMatchType = match.getType();
            if (!(newMatchType == TokenType.OP ||
                    newMatchType == TokenType.TREE_VALUE_OP ||
                    newMatchType == TokenType.FUNCTION ||
                    newMatchType == TokenType.TREE_VALUE_FUNCTION)) throw new ParseException("mismatched parentheses");
            output.add(tokenStack.pop());
        }
        return output;
    }

    /**
     * Constructs a tree recursively from a list of tokens.
     *
     * @param matches the list of tokens from the source string.
     * @return the construct tree expression.
     */
    public TreeNode constructRecursive(List<? extends Match<TokenType>> matches) {
        if (matches.size() == 0) throw new ParseException("no tokens left in input");
        Match<TokenType> match = matches.remove(0);
        TokenType matchType = match.getType();
        if (matchType == TokenType.OP || matchType == TokenType.TREE_VALUE_OP) {
            String operator = match.getContent();
            OperatorType type = typeMap.get(operator);
            if (type == OperatorType.BINARY_INFIX) {
                TreeNode right = constructRecursive(matches);
                TreeNode left = constructRecursive(matches);
                if (matchType == TokenType.OP) {
                    return new NumberBinaryNode(operator, left, right);
                } else {
                    return new TreeValueBinaryNode(operator, left, right);
                }
            } else {
                TreeNode applyTo = constructRecursive(matches);
                if (matchType == TokenType.OP) {
                    return new NumberUnaryNode(operator, applyTo);
                } else {
                    return new TreeValueUnaryNode(operator, applyTo);
                }
            }
        } else if (matchType == TokenType.NUM) {
            return new NumberNode(match.getContent());
        } else if (matchType == TokenType.VARIABLE) {
            return new VariableNode(match.getContent());
        } else if (matchType == TokenType.FUNCTION || matchType == TokenType.TREE_VALUE_FUNCTION) {
            String functionName = match.getContent();
            List<TreeNode> children = new ArrayList<>();
            while (!matches.isEmpty() && matches.get(0).getType() != TokenType.INTERNAL_FUNCTION_END) {
                TreeNode argument = constructRecursive(matches);
                children.add(0, argument);
            }
            if (matches.isEmpty()) throw new ParseException("incorrectly formatted function call");
            matches.remove(0);
            CallNode node;
            if (matchType == TokenType.FUNCTION) {
                node = new NumberFunctionNode(functionName, children);
            } else {
                node = new TreeValueFunctionNode(functionName, children);
            }
            return node;
        }
        throw new ParseException("unrecognized token");
    }

    @Override
    public TreeNode constructTree(List<? extends Match<TokenType>> tokens) {
        if (tokens.isEmpty()) throw new ParseException("no input tokens");
        tokens = intoPostfix(new ArrayList<>(tokens));
        Collections.reverse(tokens);
        TreeNode constructedTree = constructRecursive(tokens);
        if(tokens.size() == 0) return constructedTree;
        throw new ParseException("could not parse all input");
    }

    @Override
    public void onLoad(PluginManager manager) {
        for (String operator : manager.getAllOperators()) {
            Operator operatorInstance = manager.operatorFor(operator);
            precedenceMap.put(operator, operatorInstance.getPrecedence());
            associativityMap.put(operator, operatorInstance.getAssociativity());
            typeMap.put(operator, operatorInstance.getType());
        }
        for (String operator : manager.getAllTreeValueOperators()) {
            Operator operatorInstance = manager.treeValueOperatorFor(operator);
            precedenceMap.put(operator, operatorInstance.getPrecedence());
            associativityMap.put(operator, operatorInstance.getAssociativity());
            typeMap.put(operator, operatorInstance.getType());
        }
    }

    @Override
    public void onUnload(PluginManager manager) {
        precedenceMap.clear();
        associativityMap.clear();
        typeMap.clear();
    }
}
