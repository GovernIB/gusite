/**
 *
 */
package es.caib.gusite.front.general.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Transfer Object que contendrá el resultado de una búsqueda paginada
 * 
 * @author agarcia
 */
public class ResultadoBusqueda<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5582328230300652139L;

	private Map<String, Integer> parametros = new Hashtable<String, Integer>();

	/**
	 * El número total de registros
	 */
	private Integer totalNumRecords = 0;

	/**
	 * indica si se ha producido un error
	 */
	private boolean error = false;

	/**
	 * Identificador del error
	 */
	private ErrorMicrosite errorId;

	/**
	 * Mensaje de error
	 */
	private String msg;

	public ResultadoBusqueda() {
	}

	public ResultadoBusqueda(List<T> datos, int size) {
		this.setResultados(datos);
		this.setTotalNumRecords(size);
	}

	public ResultadoBusqueda(List<T> datos) {
		if (datos != null) {
			this.setResultados(datos);
			this.setTotalNumRecords(datos.size());
		}
	}

	public ResultadoBusqueda(List<T> datos, Map<String, Integer> parametros) {
		this.setResultados(datos);
		this.setParametros(parametros);
		this.setTotalNumRecords(parametros.get("nreg"));
	}

	public ResultadoBusqueda(ErrorMicrosite errorMicrosite) {
		this.error = true;
		this.errorId = errorMicrosite;
	}

	/**
	 * Resultados de la búsqueda. Contendrá una sola página de resultados.
	 */
	private Collection<T> resultados;

	public Integer getTotalNumRecords() {
		return this.totalNumRecords;
	}

	/**
	 * Fija totalNumRecords
	 * 
	 * @param totalNumRecords
	 */
	public void setTotalNumRecords(Integer totalNumRecords) {
		this.totalNumRecords = totalNumRecords;
	}

	public boolean isError() {
		return this.error;
	}

	/**
	 * Fija error
	 * 
	 * @param error
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	public ErrorMicrosite getErrorId() {
		return this.errorId;
	}

	/**
	 * Fija errorId
	 * 
	 * @param errorId
	 */
	public void setErrorId(ErrorMicrosite errorId) {
		this.errorId = errorId;
	}

	public String getMsg() {
		return this.msg;
	}

	/**
	 * Fija el mensaje de error
	 * 
	 * @param msg
	 *            el mensaje de error
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Collection<T> getResultados() {
		return this.resultados;
	}

	/**
	 * Fija los resultados
	 * 
	 * @param resultados
	 *            los resultados
	 */
	public void setResultados(Collection<T> resultados) {
		this.resultados = resultados;
	}

	public void setParametros(Map<String, Integer> parametros) {
		this.parametros = parametros;
	}

	public Map<String, Integer> getParametros() {
		return this.parametros;
	}

}
