package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class MemoryRepository implements TreeRepository {


    private final Map<Integer, TreeNode> store = new HashMap<>();

    @Override
    public void save(TreeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("El nodo no puede ser null");
        }
        store.put(node.getId(), node);
    }

    @Override
    public List<TreeNode> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<TreeNode> findById(int id) {
        return Optional.ofNullable(store.get(id));
    }
}