package org.ibit.rol.sac.microback.actionform.busca;

/**
 *	Formulario para las funcionalidades de búsqueda y ordenación en estadísticas
 *
 *@author Indra
 *
 */
public class BuscaOrdenaEstadisticaGenActionForm extends BuscaOrdenaActionForm {
	
	private static final long serialVersionUID = 2527385036685388989L;
	private String filtro2;
	
	public BuscaOrdenaEstadisticaGenActionForm()   {  }

	public String getFiltro2() {
		return filtro2;
	}

	public void setFiltro2(String filtro2) {
		this.filtro2 = filtro2;
	}

}
