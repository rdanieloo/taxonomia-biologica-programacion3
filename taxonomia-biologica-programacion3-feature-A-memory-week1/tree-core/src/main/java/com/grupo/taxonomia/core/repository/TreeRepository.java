package com.grupo.taxonomia.core.repository;

import com.grupo.taxonomia.core.model.TreeNode;
import java.util.Optional;

public interface TreeRepository {

    TreeNode createRoot(String value);

    TreeNode addChild(Long parentId, String value);

    Optional<TreeNode> findById(Long id);

    TreeNode getTree();
}