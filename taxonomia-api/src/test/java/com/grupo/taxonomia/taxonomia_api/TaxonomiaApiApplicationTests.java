package com.grupo.taxonomia.taxonomia_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.tree-strategy=custom",
        "app.storage=memory"
})
class TaxonomiaApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
