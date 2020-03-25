package dk.medcom.vdx.organisation.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.medcom.vdx.organisation.context.impl.SessionData;

public class TestTest {

	
	@Test
	public void test() throws JsonProcessingException {
		
		SessionData sd = new SessionData();
		sd.setUserAttributes(new HashMap<String, List<String>>());
		List<String> list1 = new LinkedList<String>();
		list1.add("kuk");
		sd.getUserAttributes().put("bla", list1);

		List<String> list2 = new LinkedList<String>();
		list2.add("t1");
		list2.add("t2");

		sd.getUserAttributes().put("ter", list2);

		ObjectMapper m = new ObjectMapper();
		String s = m.writeValueAsString(sd);
	}
}
