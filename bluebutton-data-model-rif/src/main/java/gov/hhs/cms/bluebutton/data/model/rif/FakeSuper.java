package gov.hhs.cms.bluebutton.data.model.rif;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "`FakeSupers`")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FakeSuper {
	@Id
	@Column(name = "`id`", nullable = false)
	private int id;

	@Column(name = "`group`", nullable = false, length = 64)
	private String group;

	FakeSuper() {
		this(-1, null);
	}

	public FakeSuper(int id, String group) {
		this.id = id;
		this.group = group;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}
}
