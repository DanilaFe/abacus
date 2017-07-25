package org.nwapw.abacus.lexing.pattern;

public class PatternChain<T> {

    public PatternNode<T> head;
    public PatternNode<T> tail;

    public PatternChain(PatternNode<T> head, PatternNode<T> tail){
        this.head = head;
        this.tail = tail;
    }

    public PatternChain(PatternNode<T> node){
        this(node, node);
    }

    public PatternChain(){
        this(null);
    }

    public void append(PatternChain<T> other){
        if(other.head == null || tail == null) {
            this.head = other.head;
            this.tail = other.tail;
        } else {
            tail.outputStates.add(other.head);
            tail = other.tail;
        }
    }

    public void append(PatternNode<T> node){
        if(tail == null){
            head = tail = node;
        } else {
            tail.outputStates.add(node);
            tail = node;
        }
    }

}
