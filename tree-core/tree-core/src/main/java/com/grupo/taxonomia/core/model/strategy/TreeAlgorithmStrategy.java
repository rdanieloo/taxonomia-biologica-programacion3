package com.grupo.taxonomia.core.model.strategy;

import com.grupo.taxonomia.core.model.TreeNode;
import java.util.List;

/**
 * Contrato del motor de algoritmos sobre árboles jerárquicos.
 * Las operaciones de mutación (crear raíz, agregar hijo) están en {@link com.grupo.taxonomia.core.repository.TreeRepository};
 * esta interfaz define recorridos y consultas sobre la estructura en memoria.
 */
public interface TreeAlgorithmStrategy {

    /**
     * Recorrido en profundidad (pre-order) desde la raíz.
     *
     * @param root nodo raíz del árbol
     * @return lista de nodos en orden DFS
     */
    List<TreeNode> dfs(TreeNode root);

    /**
     * Recorrido por niveles (BFS) desde la raíz.
     *
     * @param root nodo raíz del árbol
     * @return lista de nodos en orden BFS
     */
    List<TreeNode> bfs(TreeNode root);

    /**
     * Altura del árbol: 0 si está vacío o solo tiene raíz.
     *
     * @param root nodo raíz
     * @return altura máxima en aristas desde la raíz
     */
    int calculateHeight(TreeNode root);

    /**
     * Valida que no existan ciclos en la estructura.
     *
     * @param root nodo raíz
     * @return {@code true} si el árbol es válido (sin ciclos)
     */
    boolean validateNoCycles(TreeNode root);

    /**
     * Profundidad de un nodo: 0 para la raíz.
     *
     * @param root   nodo raíz del árbol
     * @param nodeId identificador del nodo
     * @return profundidad o -1 si no existe
     */
    int calculateDepth(TreeNode root, Long nodeId);

    /**
     * Ancestros del nodo (padre hasta raíz), sin incluir el nodo consultado.
     *
     * @param root   nodo raíz
     * @param nodeId identificador del nodo
     * @return lista de ancestros, del más cercano al más lejano
     */
    List<TreeNode> getAncestors(TreeNode root, Long nodeId);

    /**
     * Subárbol cuya raíz es el nodo indicado.
     *
     * @param root   nodo raíz del árbol completo
     * @param nodeId identificador del nodo raíz del subárbol
     * @return subárbol o {@code null} si no existe
     */
    TreeNode getSubtree(TreeNode root, Long nodeId);

    /**
     * Camino desde la raíz hasta el nodo (inclusive).
     *
     * @param root   nodo raíz
     * @param nodeId identificador del nodo destino
     * @return lista raíz → … → nodo
     */
    List<TreeNode> getPathToNode(TreeNode root, Long nodeId);
}
