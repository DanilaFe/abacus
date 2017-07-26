package org.nwapw.abacus.lexing.pattern;

/**
 * A chain of nodes that can be treated as a single unit.
 * Used during pattern compilation.
 * @param <T> the type used to identify which pattern has been matched.
 */
public class PatternChain<T> {

    /**
     * The head node of the chain.
     */
    public PatternNode<T> head;
    /**
     * The tail node of the chain.
     */
    public PatternNode<T> tail;

    /**
     * Creates a new chain with the given start and end.
     * @param head the start of the chain.
     * @param tail the end of the chain.
     */
    public PatternChain(PatternNode<T> head, PatternNode<T> tail){
        this.head = head;
        this.tail = tail;
    }

    /**
     * Creates a chain that starts and ends with the same node.
     * @param node the node to use.
     */
    public PatternChain(PatternNode<T> node){
        this(node, node);
    }

    /**
     * Creates an empty chain.
     */
    public PatternChain(){
        this(null);
    }

    /**
     * Appends the other chain to this one. This modifies
     * the nodes, as well.
     * If this chain is empty, it is set to the other.
     * @param other the other chain to append.
     */
    public void append(PatternChain<T> other){
        if(other.head == null || tail == null) {
            this.head = other.head;
            this.tail = other.tail;
        } else {
            tail.outputStates.add(other.head);
            tail = other.tail;
        }
    }

    /**
     * Appends a single node to this chain. This modifies
     * the nodes, as well.
     * If this chain is empty, it is set to the node.
     * @param node the node to append to this chain.
     */
    public void append(PatternNode<T> node){
        if(tail == null){
            head = tail = node;
        } else {
            tail.outputStates.add(node);
            tail = node;
        }
    }

}
