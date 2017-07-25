package org.nwapw.abacus.lexing;

import org.nwapw.abacus.lexing.pattern.EndNode;
import org.nwapw.abacus.lexing.pattern.Match;
import org.nwapw.abacus.lexing.pattern.Pattern;
import org.nwapw.abacus.lexing.pattern.PatternNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Lexer<T> {

    private ArrayList<Pattern<T>> patterns;

    public Lexer(){
        patterns = new ArrayList<>();
    }

    public void register(String pattern, T id){
        Pattern<T> compiledPattern = new Pattern<>(pattern, id);
        if(compiledPattern.getHead() != null) patterns.add(compiledPattern);
    }

    public Match<T> lexOne(String from, int startAt, Comparator<T> compare){
        ArrayList<Match<T>> matches = new ArrayList<>();
        HashSet<PatternNode<T>> currentSet = new HashSet<>();
        HashSet<PatternNode<T>> futureSet = new HashSet<>();
        int index = startAt;
        for(Pattern<T> pattern : patterns){
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

    public ArrayList<Match<T>> lexAll(String from, int startAt, Comparator<T> compare){
        int index = startAt;
        ArrayList<Match<T>> matches = new ArrayList<>();
        Match<T> lastMatch = null;
        while((lastMatch = lexOne(from, index, compare)) != null && index < from.length()){
            if(lastMatch.getTo() == lastMatch.getFrom()) return null;
            matches.add(lastMatch);
            index += lastMatch.getTo() - lastMatch.getFrom();
        }
        return matches;
    }

}
