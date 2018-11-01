package bean;

import model.NODE;

public class NODESERVICEBean {
		private String nodeID;
		private String type;
		private String start;
		private String stop;
		private String mdfUser;
		private String mdfTime;
		
		public NODESERVICEBean(){}
		public NODESERVICEBean(String nodeID, String type, String start,
				String stop, String mdfUser, String mdfTime) {
			super();
			this.nodeID = nodeID;
			this.type = type;
			this.start = start;
			this.stop = stop;
			this.mdfUser = mdfUser;
			this.mdfTime = mdfTime;
		}

		public NODESERVICEBean(String nodeID, String type, String start,
				String stop, String mdfUser) {
			super();
			this.nodeID = nodeID;
			this.type = type;
			this.start = start;
			this.stop = stop;
			this.mdfUser = mdfUser;
		}

		public String getNodeID() {
			return nodeID;
		}

		public void setNodeID(String nodeID) {
			this.nodeID = nodeID;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getStop() {
			return stop;
		}

		public void setStop(String stop) {
			this.stop = stop;
		}

		public String getMdfUser() {
			return mdfUser;
		}

		public void setMdfUser(String mdfUser) {
			this.mdfUser = mdfUser;
		}

		public String getMdfTime() {
			return mdfTime;
		}

		public void setMdfTime(String mdfTime) {
			this.mdfTime = mdfTime;
		}
		
		
		public void inroll(){
			NODE.setNodeService(this);
		}
}
