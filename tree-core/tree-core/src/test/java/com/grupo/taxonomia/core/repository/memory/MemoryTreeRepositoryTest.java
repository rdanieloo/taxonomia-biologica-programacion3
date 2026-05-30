package com.grupo.taxonomia.core.repository.memory;

import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.exception.NodeNotFoundException;
import com.grupo.taxonomia.core.exception.ParentNodeNotFoundException;
import com.grupo.taxonomia.core.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryTreeRepositoryTest {

    private MemoryTreeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryTreeRepository();
    }

    @Test
    void createRootAssignsIdOne() {
        TreeNode root = repository.createRoot("Raíz");
        assertEquals(1L, root.getId());
    }

    @Test
    void duplicateRootThrows() {
        repository.createRoot("Raíz");
        assertThrows(InvalidTreeStateException.class, () -> repository.createRoot("Otra"));
    }

    @Test
    void addChildToMissingParentThrows() {
        repository.createRoot("Raíz");
        assertThrows(ParentNodeNotFoundException.class,
                () -> repository.addChild(99L, "Hijo"));
    }

    @Test
    void deleteNodeRemovesSubtree() {
        TreeNode root = repository.createRoot("Raíz");
        TreeNode child = repository.addChild(root.getId(), "Hijo");
        repository.deleteNode(child.getId());
        assertTrue(repository.findById(child.getId()).isEmpty());
    }

    @Test
    void updateNodeChangesValue() {
        TreeNode root = repository.createRoot("Antes");
        repository.updateNode(root.getId(), "Después");
        assertEquals("Después", repository.findById(root.getId()).orElseThrow().getValue());
    }

    @Test
    void updateMissingNodeThrows() {
        assertThrows(NodeNotFoundException.class,
                () -> repository.updateNode(1L, "X"));
    }
}
