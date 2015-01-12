package org.ibit.rol.sac.microback.utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.ibit.rol.sac.microback.Microback;

/**
 * Clase que contiene diferentes utilidades relacionadas con Strings
 * 
 * @author Indra
 * 
 */
public class Cadenas {

	/**
	 * Método que divide el string en palabras y las pasa a un arraylist
	 * 
	 * @param cadena
	 * @return ArrayList
	 */
	public static ArrayList<String> getArrayListFromString(String cadena) {

		ArrayList<String> lista = new ArrayList<String>();
		if (cadena.length() > 0) {
			String txseparador = "" + Microback.separatorwords;
			String[] listastringcadenas = cadena.split(txseparador);
			for (int i = 0; i < listastringcadenas.length; i++)
				if (listastringcadenas[i].length() > 0)
					lista.add(listastringcadenas[i]);
		}

		return lista;
	}

	/**
	 * Método que divide el string en palabras y las pasa a un hashtable
	 * 
	 * @param cadena
	 * @return Hashtable
	 */
	public static Hashtable<String, String> getHashtableFromString(String cadena) {
		Hashtable<String, String> listahash = new Hashtable<String, String>();

		if (cadena.length() > 0) {
			String txseparador = "" + Microback.separatorwords;
			String[] listastringcadenas = cadena.split(txseparador);
			for (int i = 0; i < listastringcadenas.length; i++)
				if (listastringcadenas[i].length() > 0)
					listahash.put(listastringcadenas[i], listastringcadenas[i]);
		}
		return listahash;
	}

	/**
	 * Método que dada una URI devuelve el valor de un parametro.
	 * 
	 * @param cadena
	 *            en la que hay que buscar el parametro
	 * @param parametro
	 *            en cuestión
	 * @return String con el valor del parametro solicitado
	 */
	public static String getValueParameter(String cadena, String parametro) {
		String retorno = "-1";

		cadena = (cadena.indexOf("?") != -1) ? cadena.substring(
				cadena.indexOf("?") + 1, cadena.length()) : "";

		cadena = cadena.toLowerCase();
		String str[] = null;
		String strpropertyid[] = null;

		StringTokenizer st = new StringTokenizer(cadena, "&");
		int n = st.countTokens();

		str = new String[n];
		for (int i = 0; i < n; i++) {
			str[i] = st.nextToken();
			int pos_propertyid = str[i].indexOf(parametro);
			if (pos_propertyid != -1) {
				StringTokenizer stprop = new StringTokenizer(str[i], "=");
				int nprop = stprop.countTokens();
				strpropertyid = new String[nprop];
				strpropertyid[0] = stprop.nextToken();
				strpropertyid[1] = stprop.nextToken();
				if (nprop == 2) {
					retorno = strpropertyid[1];
					break;
				}

			}
		}

		return retorno;
	}

	/**
	 * Método que parsea substituyendo los saltos de linea por tags BR
	 * 
	 * @param lineas
	 * @return String
	 */
	public static String CRtoBR(String lineas) {
		String result = "";
		StringTokenizer st = new StringTokenizer(lineas, "\n");
		if (st.countTokens() > 0) {
			result += st.nextToken();
			while (st.hasMoreTokens())
				result += "<br/>" + st.nextToken();
		}
		return result;
	}

	/**
	 * Método que cuenta el número de veces que aparece un substring en un
	 * string
	 * 
	 * @param frase
	 *            string
	 * @param substr
	 *            substring
	 * @return int
	 */
	public static int countSubstr(String frase, String substr) {
		int cont = 0;
		int posicion = frase.indexOf(substr);
		while (posicion != -1) {
			cont = cont + 1;
			frase = frase.substring(posicion + substr.length()); // (*)
			posicion = frase.indexOf(substr);
		}
		return cont;
	}
}