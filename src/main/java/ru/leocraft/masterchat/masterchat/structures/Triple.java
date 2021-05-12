package ru.leocraft.masterchat.masterchat.structures;

public class Triple<T, K, V> {
    private T v1;
    private K v2;
    private V v3;

    public Triple(T v1, K v2, V v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public T getV1() {
        return v1;
    }

    public void setV1(T v1) {
        this.v1 = v1;
    }

    public K getV2() {
        return v2;
    }

    public void setV2(K v2) {
        this.v2 = v2;
    }

    public V getV3() {
        return v3;
    }

    public void setV3(V v3) {
        this.v3 = v3;
    }

}
