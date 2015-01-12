package org.ibit.rol.sac.microback.actionform.busca;

import java.util.List;
import java.util.Set;


/**
 *	Formulario para las funcionalidades de búsqueda y ordenación en contenidos
 *
 *@author Indra
 *
 */
public class BuscaOrdenaLDistribucionActionForm extends BuscaOrdenaActionForm
{
	private static final long serialVersionUID = -7648678325553248389L;
	private String[] seleccionados;
	private String accion;
		
	public BuscaOrdenaLDistribucionActionForm()   {  }

	public String[] getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(String[] seleccionados) {
		this.seleccionados = seleccionados;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
	
}
