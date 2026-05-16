package tree;

import java.util.List;
import java.util.Optional;

public interface TreeRepository {

    /**
     * Guarda un nodo en el repositorio.
     * Si ya existe un nodo con el mismo id, lo actualiza.
     *
     * @param node el nodo a guardar
     */
    void save(TreeNode node);

    /**
     * Retorna todos los nodos almacenados.
     *
     * @return lista de todos los nodos
     */
    List<TreeNode> findAll();

    /**
     * Busca un nodo por su id.
     *
     * @param id identificador del nodo
     * @return Optional con el nodo si existe, o vacío si no
     */
    Optional<TreeNode> findById(int id);
}