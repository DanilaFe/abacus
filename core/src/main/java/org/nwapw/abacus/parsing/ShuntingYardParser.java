package org.nwapw.abacus.parsing;

import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.OperatorAssociativity;
import org.nwapw.abacus.function.OperatorType;
import org.nwapw.abacus.lexing.pattern.Match;
import org.nwapw.abacus.plugin.PluginListener;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.tree.*;

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
            } else if (matchType == TokenType.FUNCTION) {
                output.add(new Match<>("", TokenType.INTERNAL_FUNCTION_END));
                tokenStack.push(match);
            } else if (matchType == TokenType.OP) {
                String tokenString = match.getContent();
                OperatorType type = typeMap.get(tokenString);
                int precedence = precedenceMap.get(tokenString);
                OperatorAssociativity associativity = associativityMap.get(tokenString);

                if (type == OperatorType.UNARY_POSTFIX) {
                    output.add(match);
                    continue;
                }

                if (tokenString.equals("-") && (previousType == null || previousType == TokenType.OP ||
                        previousType == TokenType.OPEN_PARENTH)) {
                    from.add(0, new Match<>("`", TokenType.OP));
                    continue;
                }

                while (!tokenStack.empty() && type == OperatorType.BINARY_INFIX) {
                    Match<TokenType> otherMatch = tokenStack.peek();
                    TokenType otherMatchType = otherMatch.getType();
                    if (!(otherMatchType == TokenType.OP || otherMatchType == TokenType.FUNCTION)) break;

                    if (otherMatchType == TokenType.OP) {
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
                if (tokenStack.empty()) return null;
                if (matchType == TokenType.CLOSE_PARENTH) {
                    tokenStack.pop();
                }
            }
        }
        while (!tokenStack.empty()) {
            Match<TokenType> match = tokenStack.peek();
            TokenType newMatchType = match.getType();
            if (!(newMatchType == TokenType.OP || newMatchType == TokenType.FUNCTION)) return null;
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
    public TreeNode constructRecursive(List<Match<TokenType>> matches) {
        if (matches.size() == 0) return null;
        Match<TokenType> match = matches.remove(0);
        TokenType matchType = match.getType();
        if (matchType == TokenType.OP) {
            String operator = match.getContent();
            OperatorType type = typeMap.get(operator);
            if (type == OperatorType.BINARY_INFIX) {
                TreeNode right = constructRecursive(matches);
                TreeNode left = constructRecursive(matches);
                if (left == null || right == null) return null;
                else return new BinaryNode(operator, left, right);
            } else {
                TreeNode applyTo = constructRecursive(matches);
                if (applyTo == null) return null;
                else return new UnaryNode(operator, applyTo);
            }
        } else if (matchType == TokenType.NUM) {
            return new NumberNode(match.getContent());
        } else if (matchType == TokenType.VARIABLE) {
            return new VariableNode(match.getContent());
        } else if (matchType == TokenType.FUNCTION) {
            String functionName = match.getContent();
            FunctionNode node = new FunctionNode(functionName);
            while (!matches.isEmpty() && matches.get(0).getType() != TokenType.INTERNAL_FUNCTION_END) {
                TreeNode argument = constructRecursive(matches);
                if (argument == null) return null;
                node.getChildren().add(0, argument);
            }
            if (matches.isEmpty()) return null;
            matches.remove(0);
            return node;
        }
        return null;
    }

    @Override
    public TreeNode constructTree(List<Match<TokenType>> tokens) {
        tokens = intoPostfix(new ArrayList<>(tokens));
        if (tokens == null) return null;
        Collections.reverse(tokens);
        TreeNode constructedTree = constructRecursive(tokens);
        return tokens.size() == 0 ? constructedTree : null;
    }

    @Override
    public void onLoad(PluginManager manager) {
        for (String operator : manager.getAllOperators()) {
            Operator operatorInstance = manager.operatorFor(operator);
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
