package com.grupo.taxonomia.taxonomia_api.controller;

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

    private final TreeService service;

    public TreeQueryController(TreeService service) {
        this.service = service;
    }

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