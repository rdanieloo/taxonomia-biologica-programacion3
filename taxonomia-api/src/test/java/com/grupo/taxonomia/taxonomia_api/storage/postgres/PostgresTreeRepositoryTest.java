package com.grupo.taxonomia.taxonomia_api.storage.postgres;

import com.grupo.taxonomia.core.model.TreeNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ActiveProfiles("test")
@Import(PostgresTreeRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/db/init-h2.sql", "/db/data-h2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PostgresTreeRepositoryTest {

    @Autowired
    private PostgresTreeRepository repository;

    @Test
    void findByIdLoadsNode() {
        Optional<TreeNode> node = repository.findById(7L);
        assertTrue(node.isPresent());
        assertEquals("Homo sapiens", node.get().getValue());
    }

    @Test
    void getTreeBuildsHierarchy() {
        TreeNode root = repository.getTree();
        assertEquals("Animalia", root.getValue());
        assertEquals(2, root.getChildren().size());
    }

    @Test
    void findChildrenReturnsDirectDescendants() {
        List<TreeNode> children = repository.findChildren(1L);
        assertEquals(2, children.size());
    }

    @Test
    void findRoot() {
        Optional<TreeNode> root = repository.findRoot();
        assertTrue(root.isPresent());
        assertEquals(1L, root.get().getId());
    }

    @Test
    void updateChangesValue() {
        repository.updateNode(7L, "Homo sapiens sapiens");
        assertEquals("Homo sapiens sapiens", repository.findById(7L).orElseThrow().getValue());
    }
}
