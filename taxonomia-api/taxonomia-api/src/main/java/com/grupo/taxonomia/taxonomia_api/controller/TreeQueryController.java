package com.grupo.taxonomia.taxonomia_api.controller;

import com.grupo.taxonomia.core.model.TreeNodeDTO;
import com.grupo.taxonomia.taxonomia_api.core.service.TreeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TreeQueryController {

    private final TreeService service;

    public TreeQueryController(TreeService service) {
        this.service = service;
    }

    @GetMapping("/tree")
    public TreeNodeDTO getTree() {
        return service.getTree();
    }
}
