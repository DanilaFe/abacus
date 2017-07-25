package org.nwapw.abacus.lexing.pattern;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Function;

public class Pattern<T> {

    private T id;
    private PatternNode<T> head;
    private String source;
    private int index;

    private HashMap<Character, Function<PatternChain<T>, PatternChain<T>>> operations =
            new HashMap<Character, Function<PatternChain<T>, PatternChain<T>>>() {{
                put('+', Pattern.this::transformPlus);
                put('*', Pattern.this::transformStar);
                put('?', Pattern.this::transformQuestion);
            }};

    private PatternChain<T> transformPlus(PatternChain<T> chain){
        chain.tail.outputStates.add(chain.head);
        return chain;
    }

    private PatternChain<T> transformStar(PatternChain<T> chain){
        LinkNode<T> newTail = new LinkNode<>();
        LinkNode<T> newHead = new LinkNode<>();
        newHead.outputStates.add(chain.head);
        newHead.outputStates.add(newTail);
        chain.tail.outputStates.add(newTail);
        newTail.outputStates.add(newHead);
        chain.head = newHead;
        chain.tail = newTail;
        return chain;
    }

    private PatternChain<T> transformQuestion(PatternChain<T> chain){
        LinkNode<T> newTail = new LinkNode<>();
        LinkNode<T> newHead = new LinkNode<>();
        newHead.outputStates.add(chain.head);
        newHead.outputStates.add(newTail);
        chain.tail.outputStates.add(newTail);
        chain.head = newHead;
        chain.tail = newTail;
        return chain;
    }

    private PatternChain<T> combineChains(Collection<PatternChain<T>> collection){
        LinkNode<T> head = new LinkNode<>();
        LinkNode<T> tail = new LinkNode<>();
        PatternChain<T> newChain = new PatternChain<>(head, tail);
        for(PatternChain<T> chain : collection){
            head.outputStates.add(chain.head);
            chain.tail.outputStates.add(tail);
        }
        return newChain;
    }

    private PatternChain<T> parseValue(){
        if(index >= source.length()) return null;
        if(source.charAt(index) == '\\'){
            if(++index >= source.length()) return null;
        }
        return new PatternChain<>(new ValueNode<>(source.charAt(index++)));
    }

    private PatternChain<T> parseOr(){
        Stack<PatternChain<T>> orStack = new Stack<>();
        index++;
        while(index < source.length() && source.charAt(index) != ']'){
            if(source.charAt(index) == '-'){
                index++;
                if(orStack.empty() || orStack.peek().tail.range() == '\0') return null;
                PatternChain<T> bottomRange = orStack.pop();
                PatternChain<T> topRange = parseValue();
                if(topRange == null || topRange.tail.range() == '\0') return null;

                orStack.push(new PatternChain<>(new RangeNode<>(bottomRange.tail.range(), topRange.tail.range())));
            } else {
                PatternChain<T> newChain = parseValue();
                if(newChain == null) return null;
                orStack.push(newChain);
            }
        }
        if(index++ >= source.length()) return null;
        return (orStack.size() == 1) ? orStack.pop() : combineChains(orStack);
    }

    private PatternChain<T> parseSegment(boolean isSubsegment){
        if(index >= source.length() || ((source.charAt(index) != '(') && isSubsegment)) return null;
        if(isSubsegment) index++;

        Stack<PatternChain<T>> orChain = new Stack<>();
        PatternChain<T> fullChain = new PatternChain<>();
        PatternChain<T> currentChain = null;
        while (index < source.length() && source.charAt(index) != ')'){
            char currentChar = source.charAt(index);
            if(operations.containsKey(currentChar)){
                if(currentChain == null) return null;

                currentChain = operations.get(currentChar).apply(currentChain);
                fullChain.append(currentChain);
                currentChain = null;
                index++;
            } else if(currentChar == '|'){
                if(currentChain == null) return null;

                fullChain.append(currentChain);
                orChain.push(fullChain);
                currentChain = null;
                fullChain = new PatternChain<>();
            } else if(currentChar == '('){
                if(currentChain != null) {
                    fullChain.append(currentChain);
                }

                currentChain = parseSegment(true);
                if(currentChain == null) return null;
            } else if(currentChar == '['){
                if(currentChain != null){
                    fullChain.append(currentChain);
                }
                currentChain = parseOr();
                if(currentChain == null) return null;
            } else if(currentChar == '.'){
                if(currentChain != null){
                    fullChain.append(currentChain);
                }
                currentChain = new PatternChain<>(new AnyNode<>());
                index++;
            } else {
                if(currentChain != null){
                    fullChain.append(currentChain);
                }
                currentChain = parseValue();
                if(currentChain == null) return null;
            }
        }

        if(!(!isSubsegment || (index < source.length() && source.charAt(index) == ')'))) return null;
        if(isSubsegment) index++;

        if(currentChain != null) fullChain.append(currentChain);
        if(!orChain.empty()){
            orChain.push(fullChain);
            fullChain = combineChains(orChain);
        }

        return fullChain;
    }

    public Pattern(String from, T id){
        this.id = id;
        index = 0;
        source = from;

        PatternChain<T> chain = parseSegment(false);
        if(chain == null) {
            head = null;
        } else {
            chain.append(new EndNode<>(id));
            head = chain.head;
        }
    }

    public PatternNode<T> getHead() {
        return head;
    }
}
