package org.nwapw.abacus.lexing.pattern;

import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Function;

/**
 * A pattern that can be compiled from a string and used in lexing.
 * @param <T> the type that is used to identify and sort this pattern.
 */
public class Pattern<T> {

    /**
     * The ID of this pattern.
     */
    private T id;
    /**
     * The head of this pattern.
     */
    private PatternNode<T> head;
    /**
     * The source string of this pattern.
     */
    private String source;
    /**
     * The index at which the compilation has stopped.
     */
    private int index;

    /**
     * A map of regex operator to functions that modify a PatternChain
     * with the appropriate operation.
     */
    private HashMap<Character, Function<PatternChain<T>, PatternChain<T>>> operations =
            new HashMap<Character, Function<PatternChain<T>, PatternChain<T>>>() {{
                put('+', Pattern.this::transformPlus);
                put('*', Pattern.this::transformStar);
                put('?', Pattern.this::transformQuestion);
            }};

    /**
     * A regex operator function that turns the chain
     * into a one-or-more chain.
     * @param chain the chain to transform.
     * @return the modified chain.
     */
    private PatternChain<T> transformPlus(PatternChain<T> chain){
        chain.tail.outputStates.add(chain.head);
        return chain;
    }

    /**
     * A regex operator function that turns the chain
     * into a zero-or-more chain.
     * @param chain the chain to transform.
     * @return the modified chain.
     */
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

    /**
     * A regex operator function that turns the chain
     * into a zero-or-one chain.
     * @param chain the chain to transform.
     * @return the modified chain.
     */
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

    /**
     * Combines a collection of chains into one OR chain.
     * @param collection the collection of chains to combine.
     * @return the resulting OR chain.
     */
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

    /**
     * Parses a single value from the input into a chain.
     * @return the resulting chain, or null on error.
     */
    private PatternChain<T> parseValue(){
        if(index >= source.length()) return null;
        if(source.charAt(index) == '\\'){
            if(++index >= source.length()) return null;
        }
        return new PatternChain<>(new ValueNode<>(source.charAt(index++)));
    }

    /**
     * Parses a [] range from the input into a chain.
     * @return the resulting chain, or null on error.
     */
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

    /**
     * Parses a repeatable segment from the input into a chain
     * @param isSubsegment whether the segment is a sub-expression "()", and therefore
     *                     whether to expect a closing brace.
     * @return the resulting chain, or null on error.
     */
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
                if(++index >= source.length()) return null;
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

    /**
     * Creates / compiles a new pattern with the given id from the given string.
     * @param from the string to compile a pattern from.
     * @param id the ID to use.
     */
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

    /**
     * Gets the head PatternNode, for use in matching
     * @return the pattern node.
     */
    public PatternNode<T> getHead() {
        return head;
    }
}
