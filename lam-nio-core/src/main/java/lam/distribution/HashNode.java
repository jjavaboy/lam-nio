package lam.distribution;
/**
* <p>
* Node of consistent hash
* </p>
* @author linanmiao
* @date 2017年9月22日
* @version 1.0
*/
public class HashNode {
	
	private final String host;
	
	private final int port;
	
	public HashNode(String host, int port){
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof HashNode))
			return false;
		HashNode other = (HashNode) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	

}
