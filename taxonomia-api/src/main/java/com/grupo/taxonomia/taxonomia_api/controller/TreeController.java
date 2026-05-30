package com.grupo.taxonomia.taxonomia_api.controller;

import com.grupo.taxonomia.core.model.TreeNodeDTO;
import com.grupo.taxonomia.taxonomia_api.core.service.TreeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nodes")
public class TreeController {

    private final TreeService service;

    public TreeController(TreeService service) {
        this.service = service;
    }

    @PostMapping("/root")
    public TreeNodeDTO createRoot(@RequestParam String value) {
        return service.createRoot(value);
    }

    @PostMapping("/{parentId}/children")
    public TreeNodeDTO addChild(@PathVariable Long parentId,
                                 @RequestParam String value) {
        return service.addChild(parentId, value);
    }

}