package de.th.koeln.authentifizierungservice;

import de.th.koeln.authentifizierungservice.clients.BenutzerGrpcClient;
import de.th.koeln.benutzerservice.grpc.BenutzerDaten;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthentifizierungServiceApplicationTests {

	@Autowired
	private BenutzerGrpcClient benutzerGrpcClient;

	@Test
	public void testCreateBenutzer() {
		BenutzerDaten benutzer = BenutzerDaten.newBuilder().setVorname("Test User").build();
		benutzerGrpcClient.createBenutzer(benutzer);
	}

	@Test
	public void testGetBenutzerBySub() {
		String sub = "test-sub-id";
		BenutzerDaten benutzer = benutzerGrpcClient.getBenutzerBySub(sub);
		System.out.println(benutzer);
	}

}
