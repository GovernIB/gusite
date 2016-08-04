package es.caib.gusite.microback.actionform.busca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *	Formulario para las funcionalidades de búsqueda y ordenación en contenidos
 *
 *@author Indra
 *
 */
public class BuscaOrdenaSeleccionarLDistribucionActionForm extends BuscaOrdenaActionForm
{
	private static final long serialVersionUID = -7648678325553248389L;
	private Long idConvocatoria;
	private String[] seleccionados;
	private List destinatarios = Collections.EMPTY_LIST;
	private String accion;
		
	public BuscaOrdenaSeleccionarLDistribucionActionForm()   {  }

	public String[] getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(String[] seleccionados) {
		this.seleccionados = seleccionados;
	}

	public List getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(List destinatarios) {
		this.destinatarios = destinatarios;
	}

	public Long getIdConvocatoria() {
		return idConvocatoria;
	}

	public void setIdConvocatoria(Long idConvocatoria) {
		this.idConvocatoria = idConvocatoria;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	
}
