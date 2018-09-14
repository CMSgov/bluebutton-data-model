package gov.hhs.cms.bluebutton.data.model.rif;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "`FakeChildB`")
public class FakeChildB extends FakeSuper {
	@Column(name = "`data`", nullable = false, length = 64)
	private String data;

	FakeChildB() {
		this(-1, null, null);
	}

	public FakeChildB(int id, String group, String data) {
		super(id, group);
		this.data = data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
}
