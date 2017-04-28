package xdean.rpc.test.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import xdean.rpc.api.IRpcConsumer;
import xdean.rpc.main.HttpRpcConsumer;
import xdean.rpc.test.model.TestBean;
import xdean.rpc.test.model.TestInterface;
import xdean.rpc.util.CommonUtil;

public class TestConsumer {
	IRpcConsumer hrc;
	TestInterface remoteObject;

	@Before
	public void setUp() throws IOException {
		hrc = new HttpRpcConsumer("http://127.0.0.1:8080/RPC-HTTP/RPC");
		remoteObject = hrc.getRemoteObject(TestInterface.class, "testObject");
	}

	@Test
	public void testToString() {
		remoteObject.toString();
	}
	
	@Test
	public void testHashCode(){
		remoteObject.hashCode();
	}
	
	@Test
	public void testSuperMethod(){
		remoteObject.accept(1);
	}

	@Test
	public void testProxy() {
		int i = 10;
		while (i-- > 0) {
			int a = CommonUtil.getRandomInt(10);
			int b = CommonUtil.getRandomInt(10);
			assertEquals(a + b, remoteObject.calc(a, b));
		}
	}

	@Test
	public void testBean() {
		TestBean bean = remoteObject.getBean("xdean");
		assertNotNull(bean);
		assertNotNull(bean.getTb());
	}

	@Test
	public void testCollection() {
		List<Integer> list = remoteObject.getList();
		assertNotNull(list);
		assertNotEquals(0, list.size());
	}

	@Test(expected = NullPointerException.class)
	public void testException() {
		remoteObject.causeException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotInterface() throws IOException {
		hrc.getRemoteObject(String.class, "testString");
	}

	@Test(expected = NullPointerException.class)
	public void testNotExisit() throws Throwable {
		try {
			hrc.getRemoteObject(TestInterface.class, "NOT_EXSITS");
		} catch (IOException e) {
			throw e.getCause();
		}
	}
}
