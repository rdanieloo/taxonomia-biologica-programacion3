package com.grupo.taxonomia.core.model.strategy.custom;

import com.grupo.taxonomia.core.domain.TaxonomiaBiologica;
import com.grupo.taxonomia.core.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomTreeStrategyTest {

    private CustomTreeStrategy strategy;
    private TreeNode root;

    @BeforeEach
    void setUp() {
        strategy = new CustomTreeStrategy();
        root = buildSampleTree();
    }

    private TreeNode buildSampleTree() {
        TreeNode r = new TreeNode(1L, TaxonomiaBiologica.ANIMALIA);
        TreeNode c2 = new TreeNode(2L, TaxonomiaBiologica.CHORDATA);
        TreeNode c3 = new TreeNode(3L, TaxonomiaBiologica.ARTHROPODA);
        TreeNode c4 = new TreeNode(4L, TaxonomiaBiologica.MAMMALIA);
        TreeNode c5 = new TreeNode(5L, TaxonomiaBiologica.AVES);
        r.addChild(c2);
        r.addChild(c3);
        c2.addChild(c4);
        c2.addChild(c5);
        return r;
    }

    @Test
    void dfsOrder() {
        List<String> values = strategy.dfs(root).stream()
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
    void bfsOrder() {
        List<String> values = strategy.bfs(root).stream()
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
    void heightOfSampleTree() {
        assertEquals(2, strategy.calculateHeight(root));
    }

    @Test
    void heightOfSingleRoot() {
        TreeNode onlyRoot = new TreeNode(1L, "Raíz");
        assertEquals(0, strategy.calculateHeight(onlyRoot));
    }

    @Test
    void depthOfLeaf() {
        assertEquals(2, strategy.calculateDepth(root, 4L));
    }

    @Test
    void ancestorsOfLeaf() {
        List<Long> ids = strategy.getAncestors(root, 4L).stream()
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        assertEquals(List.of(2L, 1L), ids);
    }

    @Test
    void pathFromRoot() {
        List<Long> ids = strategy.getPathToNode(root, 4L).stream()
                .map(TreeNode::getId)
                .collect(Collectors.toList());
        assertEquals(List.of(1L, 2L, 4L), ids);
    }

    @Test
    void validateNoCycles() {
        assertTrue(strategy.validateNoCycles(root));
    }
}
