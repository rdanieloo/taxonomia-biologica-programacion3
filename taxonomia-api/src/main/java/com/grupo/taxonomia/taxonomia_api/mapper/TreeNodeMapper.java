package com.grupo.taxonomia.taxonomia_api.mapper;

import java.util.List;
import java.util.stream.Collectors;

public class TreeNodeMapper {

    public static com.grupo.taxonomia.openapi.model.TreeNodeDTO toOpenApi(
            com.grupo.taxonomia.core.model.TreeNodeDTO dto) {

        if (dto == null) {
            return null;
        }

        com.grupo.taxonomia.openapi.model.TreeNodeDTO mapped =
                new com.grupo.taxonomia.openapi.model.TreeNodeDTO();

        mapped.setId(dto.getId());
        mapped.setValue(dto.getValue());

        if (dto.getChildren() != null) {
            mapped.setChildren(
                    dto.getChildren()
                            .stream()
                            .map(TreeNodeMapper::toOpenApi)
                            .collect(Collectors.toList())
            );
        }

        return mapped;
    }

    public static List<com.grupo.taxonomia.openapi.model.TreeNodeDTO> toOpenApiList(
            List<com.grupo.taxonomia.core.model.TreeNodeDTO> list) {

        return list.stream()
                .map(TreeNodeMapper::toOpenApi)
                .collect(Collectors.toList());
    }
}