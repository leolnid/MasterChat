package ru.leocraft.masterchat.masterchat.structures;

import java.util.Collection;
import java.util.LinkedList;

public class LimitedLinkedList<T> extends LinkedList<T> {
    private final int capacity;

    public LimitedLinkedList(int capacity) {
        this.capacity = capacity;
    }

    @Override
    @Deprecated
    public void addFirst(T textComponent) {
    }

    @Override
    public void addLast(T textComponent) {
        if (super.size() >= capacity)
            super.removeFirst();
        super.addLast(textComponent);
    }

    @Override
    public boolean add(T textComponent) {
        if (super.size() >= capacity)
            super.removeFirst();
        return super.add(textComponent);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        assert c.size() > capacity;
        while (super.size() + c.size() > capacity)
            super.removeFirst();
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        assert c.size() > capacity;
        while (super.size() + c.size() > capacity)
            super.removeFirst();
        return super.addAll(index, c);
    }

    @Override
    public void add(int index, T element) {
        if (super.size() >= capacity)
            super.removeFirst();
        super.add(index, element);
    }

    @Override
    public void push(T textComponent) {
    }
}
