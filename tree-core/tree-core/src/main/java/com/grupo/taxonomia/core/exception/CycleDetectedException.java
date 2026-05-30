package com.grupo.taxonomia.core.exception;

public class CycleDetectedException extends TreeException {

    public CycleDetectedException() {
        super("Se detectó un ciclo en el árbol");
    }
}
