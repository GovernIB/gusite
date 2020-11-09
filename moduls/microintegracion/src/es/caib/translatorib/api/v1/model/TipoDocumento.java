package es.caib.translatorib.api.v1.model;

/***
 * Enumerado
 *
 * @author Indra
 *
 */
public enum TipoDocumento {
	DOC("doc"), DOCX("docx"), PPTX("pptx"), XLSX("xlsx"), PDF("pdf"), ODT("odt"), ODS("ods"), ODP("odp"), TXT("txt"),
	HTML("html"), XML("wxml");

	final String tipo;

	TipoDocumento(final String iTipo) {
		this.tipo = iTipo;
	}

	public TipoDocumento fromString(final String iTipo) {
		TipoDocumento retorno = null;
		for (final TipoDocumento tipo : TipoDocumento.values()) {
			if (tipo.toString().equals(iTipo)) {
				retorno = tipo;
				break;
			}
		}
		return retorno;
	}

	@Override
	public String toString() {
		return tipo;
	}
}
