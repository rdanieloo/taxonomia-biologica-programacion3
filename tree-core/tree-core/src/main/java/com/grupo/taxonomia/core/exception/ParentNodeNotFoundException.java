package com.grupo.taxonomia.core.exception;

public class ParentNodeNotFoundException extends TreeException {

    public ParentNodeNotFoundException(Long parentId) {
        super("Nodo padre con id: " + parentId + " no encontrado");
    }
}
