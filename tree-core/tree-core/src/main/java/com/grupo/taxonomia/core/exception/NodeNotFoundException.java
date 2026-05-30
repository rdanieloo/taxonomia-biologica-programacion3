package com.grupo.taxonomia.core.exception;

public class NodeNotFoundException extends TreeException {

    public NodeNotFoundException(Long nodeId) {
        super("Nodo con id: " + nodeId + " no encontrado");
    }
}
