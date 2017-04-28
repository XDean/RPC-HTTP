package xdean.rpc.test.model;

import java.util.ArrayList;
import java.util.List;

public class TestImpl implements TestInterface {

	@Override
	public int calc(int a, int b) {
		return a + b;
	}

	@Override
	public TestBean getBean(String name) {
		TestBean tb = new TestBean();
		TestBean tbSon = new TestBean();
		tb.setName(name);
		tb.setTb(tbSon);
		tb.setMale(true);
		tbSon.setName(name+"'s Son");
		tbSon.setMale(false);
		return tb;
	}

	@Override
	public List<Integer> getList() {
		List<Integer> list = new ArrayList<>();
		for(int i=10;i>0;i--)
			list.add(i);
		return list;
	}

	@Override
	public void causeException() {
		throw new NullPointerException("ERROR HERE");
	}

	@Override
	public void accept(Integer t) {
		System.out.println(t);
	}
}
