package org.nwapw.abacus.lexing;

import org.nwapw.abacus.lexing.pattern.EndNode;
import org.nwapw.abacus.lexing.pattern.Match;
import org.nwapw.abacus.lexing.pattern.Pattern;
import org.nwapw.abacus.lexing.pattern.PatternNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A lexer that can generate tokens of a given type given a list of regular expressions
 * to operate on.
 * @param <T> the type used to identify which match belongs to which pattern.
 */
public class Lexer<T> {

    /**
     * The registered patterns.
     */
    private HashMap<String, Pattern<T>> patterns;

    /**
     * Creates a new lexer with no registered patterns.
     */
    public Lexer(){
        patterns = new HashMap<>();
    }

    /**
     * Registers a single pattern.
     * @param pattern the pattern regex
     * @param id the ID by which to identify the pattern.
     */
    public void register(String pattern, T id){
        Pattern<T> compiledPattern = new Pattern<>(pattern, id);
        if(compiledPattern.getHead() != null) patterns.put(pattern, compiledPattern);
    }

    /**
     * Reads one token from the given string.
     * @param from the string to read from
     * @param startAt the index to start at
     * @param compare the comparator used to sort tokens by their ID.
     * @return the best match.
     */
    public Match<T> lexOne(String from, int startAt, Comparator<T> compare){
        ArrayList<Match<T>> matches = new ArrayList<>();
        HashSet<PatternNode<T>> currentSet = new HashSet<>();
        HashSet<PatternNode<T>> futureSet = new HashSet<>();
        int index = startAt;
        for(Pattern<T> pattern : patterns.values()){
            pattern.getHead().addInto(currentSet);
        }
        while(!currentSet.isEmpty()){
            for(PatternNode<T> node : currentSet){
                if(index < from.length() && node.matches(from.charAt(index))) {
                    node.addOutputsInto(futureSet);
                } else if(node instanceof EndNode){
                    matches.add(new Match<>(startAt, index, ((EndNode<T>) node).getPatternId()));
                }
            }

            HashSet<PatternNode<T>> tmp = currentSet;
            currentSet = futureSet;
            futureSet = tmp;
            futureSet.clear();

            index++;
        }
        matches.sort((a, b) -> compare.compare(a.getType(), b.getType()));
        if(compare != null) {
            matches.sort(Comparator.comparingInt(a -> a.getTo() - a.getFrom()));
        }
        return matches.isEmpty() ? null : matches.get(matches.size() - 1);
    }

    /**
     * Reads all tokens from a string.
     * @param from the string to start from.
     * @param startAt the index to start at.
     * @param compare the comparator used to sort matches by their IDs.
     * @return the resulting list of matches, in order, or null on error.
     */
    public ArrayList<Match<T>> lexAll(String from, int startAt, Comparator<T> compare){
        int index = startAt;
        ArrayList<Match<T>> matches = new ArrayList<>();
        Match<T> lastMatch = null;
        while(index < from.length() && (lastMatch = lexOne(from, index, compare)) != null){
            if(lastMatch.getTo() == lastMatch.getFrom()) return null;
            matches.add(lastMatch);
            index += lastMatch.getTo() - lastMatch.getFrom();
        }
        if(lastMatch == null) return null;
        return matches;
    }

}
