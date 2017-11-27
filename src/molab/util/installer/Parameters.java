package molab.util.installer;

import java.util.ArrayList;
import java.util.List;

public class Parameters {

	private List<String> params;
	
	public Parameters() {
		params = new ArrayList<String>();
	}
	
	public Parameters addParam(String param) {
		params.add(param);
		return this;
	}
	
	public String[] getParams() {
		int len = params.size();
		String[] p = new String[len];
		for(int i = 0; i < len; i++) {
			p[i] = params.get(i);
		}
		return p;
	}
}
