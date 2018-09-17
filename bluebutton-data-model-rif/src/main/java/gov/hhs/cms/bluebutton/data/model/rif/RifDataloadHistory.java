package gov.hhs.cms.bluebutton.data.model.rif;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Entity that stores when each RIF dataload file is loaded to the PostgreSQL
 * database
 */
@Entity
@IdClass(RifDataloadHistory.PrimaryKey.class)
@Table(name = "`RifDataloadHistory`")
public class RifDataloadHistory {

	@Id
	@Column(name = "`recordType`", nullable = false)
	private String recordType;

	@Id
	@Column(name = "`createUpdateTimestamp`", nullable = false)
	private Timestamp createUpdateTimestamp;

	@Id
	@Column(name = "`sequenceId`", nullable = false, columnDefinition = "numeric")
	private int sequenceId;

	@Column(name = "`dataloadFilename`", nullable = false, length = 64)
	private String dataloadFilename;

	public RifDataloadHistory() {
		super();
	}

	/**
	 * @param recordType
	 * @param createUpdateTimestamp
	 * @param sequenceId
	 * @param dataloadFilename
	 */
	public RifDataloadHistory(String recordType, Timestamp createUpdateTimestamp, int sequenceId,
			String dataloadFilename) {
		super();
		this.recordType = recordType;
		this.createUpdateTimestamp = createUpdateTimestamp;
		this.sequenceId = sequenceId;
		this.dataloadFilename = dataloadFilename;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Timestamp getCreateUpdateTimestamp() {
		return createUpdateTimestamp;
	}

	public void setCreateUpdateTimestamp(Timestamp createUpdateTimestamp) {
		this.createUpdateTimestamp = createUpdateTimestamp;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getDataloadFilename() {
		return dataloadFilename;
	}

	public void setDataloadFilename(String dataloadFilename) {
		this.dataloadFilename = dataloadFilename;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RifDataloadHistory [recordType=");
		builder.append(recordType);
		builder.append(", createUpdateTimestamp=");
		builder.append(createUpdateTimestamp);
		builder.append(", sequenceId=");
		builder.append(sequenceId);
		builder.append(", dataloadFilename=");
		builder.append(dataloadFilename);
		builder.append("]");
		return builder.toString();
	}

	public static class PrimaryKey implements Serializable {

		private static final long serialVersionUID = 1;
		private String recordType;
		private Timestamp createUpdateTimestamp;
		private int sequenceId;

		public PrimaryKey() {
			super();
		}

		/**
		 * @param recordType
		 * @param createUpdateTimestamp
		 * @param sequenceId
		 */
		public PrimaryKey(String recordType, Timestamp createUpdateTimestamp, int sequenceId) {
			super();
			this.recordType = recordType;
			this.createUpdateTimestamp = createUpdateTimestamp;
			this.sequenceId = sequenceId;
		}

		public String getRecordType() {
			return recordType;
		}

		public Timestamp getCreateUpdateTimestamp() {
			return createUpdateTimestamp;
		}

		public int getSequenceId() {
			return sequenceId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(recordType, createUpdateTimestamp, sequenceId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			RifDataloadHistory other = (RifDataloadHistory) obj;
			if (Objects.deepEquals(recordType, other.recordType)) {
				return false;
			}
			if (Objects.deepEquals(createUpdateTimestamp, other.createUpdateTimestamp)) {
				return false;
			}
			if (Objects.deepEquals(sequenceId, other.sequenceId)) {
				return false;
			}
			return true;
		}
	}
}
