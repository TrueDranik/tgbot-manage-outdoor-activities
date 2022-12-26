package com.bot.sup.cache.chain;

public abstract class ChainHandler {
    private ChainHandler next;

    public void chainHandle(Enum<?> state){
        if (next != null){
            next.chainHandle(state);
        }
    }

    public ChainHandler bind (ChainHandler next){
        this.next = next;
        return next;
    }
}
