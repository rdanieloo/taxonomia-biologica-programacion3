package com.grupo.taxonomia.core.model.strategy.collections;

import com.grupo.taxonomia.core.domain.TaxonomiaBiologica;
import com.grupo.taxonomia.core.exception.InvalidTreeStateException;
import com.grupo.taxonomia.core.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionsTreeStrategyTest {

    private CollectionsTreeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new CollectionsTreeStrategy();
        strategy.createRoot(TaxonomiaBiologica.ANIMALIA);
        strategy.addChild(1L, TaxonomiaBiologica.CHORDATA);
        strategy.addChild(1L, TaxonomiaBiologica.ARTHROPODA);
        strategy.addChild(2L, TaxonomiaBiologica.MAMMALIA);
        strategy.addChild(2L, TaxonomiaBiologica.AVES);
    }

    @Test
    void dfsUsesStackOrder() {
        List<String> values = strategy.dfs(null).stream()
                .map(TreeNode::getValue)
                .collect(Collectors.toList());
        assertEquals(
                List.of(
                        TaxonomiaBiologica.ANIMALIA,
                        TaxonomiaBiologica.CHORDATA,
                        TaxonomiaBiologica.MAMMALIA,
                        TaxonomiaBiologica.AVES,
                        TaxonomiaBiologica.ARTHROPODA),
                values
        );
    }

    @Test
    void bfsUsesQueueOrder() {
        List<String> values = strategy.bfs(null).stream()
                .map(TreeNode::getValue)
                .collect(Collectors.toList());
        assertEquals(
                List.of(
                        TaxonomiaBiologica.ANIMALIA,
                        TaxonomiaBiologica.CHORDATA,
                        TaxonomiaBiologica.ARTHROPODA,
                        TaxonomiaBiologica.MAMMALIA,
                        TaxonomiaBiologica.AVES),
                values
        );
    }

    @Test
    void heightOfTree() {
        assertEquals(2, strategy.calculateHeight(null));
    }

    @Test
    void depthOfLeaf() {
        assertEquals(2, strategy.calculateDepth(null, 4L));
    }

    @Test
    void ancestorsOrder() {
        List<Long> ids = strategy.getAncestors(null, 4L).stream()
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        assertEquals(List.of(2L, 1L), ids);
    }

    @Test
    void pathFromRoot() {
        List<Long> ids = strategy.getPathToNode(null, 4L).stream()
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        assertEquals(List.of(1L, 2L, 4L), ids);
    }

    @Test
    void validateNoCycles() {
        assertTrue(strategy.validateNoCycles(null));
    }

    @Test
    void duplicateRootThrows() {
        assertThrows(InvalidTreeStateException.class, () -> strategy.createRoot("Otra"));
    }

    @Test
    void deleteRemovesSubtree() {
        strategy.deleteNode(2L);
        assertTrue(strategy.findById(4L).isEmpty());
        assertEquals(2, strategy.dfs(null).size());
    }
}
