package com.grupo.taxonomia.taxonomia_api.controller;

<<<<<<< HEAD
import com.grupo.taxonomia.openapi.api.NodesApi;
import com.grupo.taxonomia.openapi.api.TraversalApi;
import com.grupo.taxonomia.openapi.api.TreeApi;
import com.grupo.taxonomia.openapi.model.TreeNodeDTO;
import com.grupo.taxonomia.taxonomia_api.core.service.TreeService;
import com.grupo.taxonomia.taxonomia_api.mapper.TreeNodeMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@RestController
public class TreeQueryController implements TreeApi, NodesApi, TraversalApi {
=======
import com.grupo.taxonomia.core.model.TreeNodeDTO;
import com.grupo.taxonomia.taxonomia_api.core.service.TreeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
public class TreeQueryController {
>>>>>>> 7f431411b01b3421119b7fe706c8e630a4afb091

    private final TreeService service;

    public TreeQueryController(TreeService service) {
        this.service = service;
    }

<<<<<<< HEAD
    // Necesario porque las 3 interfaces tienen getRequest() por default
    // y Java no puede resolver el conflicto solo
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<TreeNodeDTO> getTree() {
        return ResponseEntity.ok(
                TreeNodeMapper.toOpenApi(service.getTree())
        );
    }

    @Override
    public ResponseEntity<TreeNodeDTO> getSubtree(Long nodeId) {
        return ResponseEntity.ok(
                TreeNodeMapper.toOpenApi(service.getSubtree(nodeId))
        );
    }

    @Override
    public ResponseEntity<Integer> getHeight() {
        return ResponseEntity.ok(service.getHeight());
    }

    @Override
    public ResponseEntity<Boolean> validateTree() {
        return ResponseEntity.ok(service.validateTree());
    }

    @Override
    public ResponseEntity<Integer> getDepth(Long nodeId) {
        return ResponseEntity.ok(service.getDepth(nodeId));
    }

    @Override
    public ResponseEntity<List<TreeNodeDTO>> getAncestors(Long nodeId) {
        return ResponseEntity.ok(
                TreeNodeMapper.toOpenApiList(service.getAncestors(nodeId))
        );
    }

    @Override
    public ResponseEntity<List<TreeNodeDTO>> getPathToNode(Long nodeId) {
        return ResponseEntity.ok(
                TreeNodeMapper.toOpenApiList(service.getPathToNode(nodeId))
        );
    }

    @Override
    public ResponseEntity<List<TreeNodeDTO>> dfsTraversal() {
        return ResponseEntity.ok(
                TreeNodeMapper.toOpenApiList(service.dfsTraversal())
        );
    }

    @Override
    public ResponseEntity<List<TreeNodeDTO>> bfsTraversal() {
        return ResponseEntity.ok(
                TreeNodeMapper.toOpenApiList(service.bfsTraversal())
        );
    }

    @Override
    public ResponseEntity<TreeNodeDTO> createRoot(String value) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                TreeNodeMapper.toOpenApi(service.createRoot(value))
        );
    }

    @Override
    public ResponseEntity<TreeNodeDTO> addChild(Long parentId, String value) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                TreeNodeMapper.toOpenApi(service.addChild(parentId, value))
        );
    }

    @Override
    public ResponseEntity<Void> deleteNode(Long nodeId) {
        service.deleteNode(nodeId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Boolean> updateNode(Long nodeId, String value) {
        return ResponseEntity.ok(service.updateNode(nodeId, value));
    }
}
=======
    @GetMapping("/tree")
    public TreeNodeDTO getTree() {
        return service.getTree();
    }
    
    @GetMapping("/traversal/dfs")
    public List<TreeNodeDTO> dfsTraversal() {
        return service.dfsTraversal();
    }
    
    @GetMapping("/traversal/bfs")
    public List<TreeNodeDTO> bfsTraversal() {
        return service.bfsTraversal();
    }
    
    @GetMapping("/height")
    public int getHeight() {
        return service.getHeight();
    }
    
    @GetMapping("/validate")
    public boolean validateTree() {
        return service.validateTree();
    }
    
    @GetMapping("/nodes/{nodeId}/depth")
    public int getDepth(@PathVariable Long nodeId) {
        return service.getDepth(nodeId);
    }
    
    @GetMapping("/nodes/{nodeId}/ancestors")
    public List<TreeNodeDTO> getAncestors(
            @PathVariable Long nodeId) {

        return service.getAncestors(nodeId);
    }
    @GetMapping("/tree/{nodeId}")
    public TreeNodeDTO getSubtree(
            @PathVariable Long nodeId) {

        return service.getSubtree(nodeId);
    }
    
    @GetMapping("/nodes/{nodeId}/path")
    public List<TreeNodeDTO> getPathToNode(
            @PathVariable Long nodeId) {

        return service.getPathToNode(nodeId);
    }
}
>>>>>>> 7f431411b01b3421119b7fe706c8e630a4afb091
