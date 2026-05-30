package com.grupo.taxonomia.taxonomia_api.config;

import com.grupo.taxonomia.core.domain.TaxonomiaBiologica;
import com.grupo.taxonomia.taxonomia_api.core.service.TreeService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BiologicalTaxonomyInitializer implements ApplicationRunner {

    private final TreeService treeService;

    public BiologicalTaxonomyInitializer(TreeService treeService) {
        this.treeService = treeService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (treeService.getTree() != null) {
            return;
        }

        // Árbol semilla para dejar la app lista para pruebas de taxonomía biológica.
        Long raiz = treeService.createRoot("Vida").getId();

        Long animalia = treeService.addChild(raiz, TaxonomiaBiologica.ANIMALIA).getId();
        Long plantae = treeService.addChild(raiz, TaxonomiaBiologica.PLANTAE).getId();

        Long chordata = treeService.addChild(animalia, TaxonomiaBiologica.CHORDATA).getId();
        Long mammalia = treeService.addChild(chordata, TaxonomiaBiologica.MAMMALIA).getId();
        Long primates = treeService.addChild(mammalia, TaxonomiaBiologica.PRIMATES).getId();
        Long hominidae = treeService.addChild(primates, TaxonomiaBiologica.HOMINIDAE).getId();
        Long homo = treeService.addChild(hominidae, TaxonomiaBiologica.HOMO).getId();
        treeService.addChild(homo, TaxonomiaBiologica.HOMO_SAPIENS);

        Long arthropoda = treeService.addChild(animalia, TaxonomiaBiologica.ARTHROPODA).getId();
        treeService.addChild(arthropoda, TaxonomiaBiologica.INSECTA);
        treeService.addChild(chordata, TaxonomiaBiologica.AVES);

        treeService.addChild(plantae, "Magnoliophyta");
    }
}
