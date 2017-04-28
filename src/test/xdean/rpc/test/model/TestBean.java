package xdean.rpc.test.model;

public class TestBean {

	private String name;
	
	private TestBean tb;
	
	private boolean isMale;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TestBean getTb() {
		return tb;
	}

	public void setTb(TestBean tb) {
		this.tb = tb;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	@Override
	public String toString() {
		return "TestBean [name=" + name + ", tb=" + tb + ", isMale=" + isMale + "]";
	}
	
	
}
