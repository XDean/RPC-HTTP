package xdean.rpc.test.model;

import java.util.List;
import java.util.function.Consumer;

public interface TestInterface extends Consumer<Integer>{
	
	public int calc(int a, int b);
	
	public TestBean getBean(String name);
	
	public List<Integer> getList();
	
	public void causeException();
}
