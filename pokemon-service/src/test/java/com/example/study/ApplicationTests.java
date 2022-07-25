package com.example.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Application")
class ApplicationTests {
	
	@Test
	@DisplayName("Teste de chamada de aplicação")
	void testeChamadaAplicacao() {
		assertDoesNotThrow(() -> Application.main(new String[] {}));
	}

}
