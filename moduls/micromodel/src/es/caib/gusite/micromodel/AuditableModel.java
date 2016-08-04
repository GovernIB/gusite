package es.caib.gusite.micromodel;

public abstract class AuditableModel implements Auditable {

	@Override
	public String getAuditKey() {
		return String.valueOf(this.getId());
	}

	public abstract Long getId();

	@Override
	public Long getIdmicrosite() {
		return null;
	}

}
