package org.nwapw.abacus.tree;

import org.nwapw.abacus.lexing.Lexer;
import org.nwapw.abacus.lexing.pattern.Match;

import java.util.*;

public abstract class TreeNode {

    private static Lexer<TokenType> lexer = new Lexer<TokenType>(){{
        register(".", TokenType.ANY);
        register("\\+|-|\\*|/|^", TokenType.OP);
        register("[0-9]+(\\.[0-9]+)?", TokenType.NUM);
        register("[a-zA-Z]+", TokenType.WORD);
        register("\\(", TokenType.OPEN_PARENTH);
        register("\\)", TokenType.CLOSE_PARENTH);
    }};
    private static HashMap<String, Integer> precedenceMap = new HashMap<String, Integer>(){{
        put("+", 0);
        put("-", 0);
        put("*", 1);
        put("/", 1);
        put("^", 2);
    }};
    private static HashMap<String, OperatorAssociativity> associativityMap =
            new HashMap<String, OperatorAssociativity>() {{
                put("+", OperatorAssociativity.LEFT);
                put("-", OperatorAssociativity.LEFT);
                put("*", OperatorAssociativity.LEFT);
                put("/", OperatorAssociativity.LEFT);
                put("^", OperatorAssociativity.RIGHT);
            }};

    private static Comparator<TokenType> tokenSorter = Comparator.comparingInt(e -> e.priority);

    public static ArrayList<Match<TokenType>> tokenize(String string){
        return lexer.lexAll(string, 0, tokenSorter);
    }

    public static ArrayList<Match<TokenType>> intoPostfix(String source, ArrayList<Match<TokenType>> from){
        ArrayList<Match<TokenType>> output = new ArrayList<>();
        Stack<Match<TokenType>> tokenStack = new Stack<>();
        while(!from.isEmpty()){
            Match<TokenType> match = from.remove(0);
            if(match.getType() == TokenType.NUM) {
                output.add(match);
            } else if(match.getType() == TokenType.OP){
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
            } else if(match.getType() == TokenType.OPEN_PARENTH){
                tokenStack.push(match);
            } else if(match.getType() == TokenType.CLOSE_PARENTH){
                while(!tokenStack.empty() && tokenStack.peek().getType() != TokenType.OPEN_PARENTH){
                    output.add(tokenStack.pop());
                }
                if(tokenStack.empty()) return null;
                tokenStack.pop();
            }
        }
        while(!tokenStack.empty()){
            if(!(tokenStack.peek().getType() == TokenType.OP)) return null;
            output.add(tokenStack.pop());
        }
        return output;
    }

    public static TreeNode fromStringRecursive(String source, ArrayList<Match<TokenType>> matches){
        if(matches.size() == 0) return null;
        Match<TokenType> match = matches.remove(0);
        if(match.getType() == TokenType.OP){
            TreeNode right = fromStringRecursive(source, matches);
            TreeNode left = fromStringRecursive(source, matches);
            if(left == null || right == null) return null;
            else return new OpNode(source.substring(match.getFrom(), match.getTo()), left, right);
        } else if(match.getType() == TokenType.NUM){
            return new NumberNode(Double.parseDouble(source.substring(match.getFrom(), match.getTo())));
        }
        return null;
    }

    public static TreeNode fromString(String string){
        ArrayList<Match<TokenType>> matches = intoPostfix(string, tokenize(string));
        if(matches == null) return null;

        Collections.reverse(matches);
        return fromStringRecursive(string, matches);
    }

}
