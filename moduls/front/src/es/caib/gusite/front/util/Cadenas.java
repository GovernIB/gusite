package es.caib.gusite.front.util;

import java.util.ArrayList;
import java.util.Hashtable;

import es.caib.gusite.front.general.Microfront;

public class Cadenas {

	private static String[][] canvis = new String[][] { { " I ", " i " }, { " O ", " o " }, { " De ", " de " },
			{ " Del ", " del " }, { " D'", " d'" }, { " A ", " a " }, { " En ", " en " }, { " El ", " el " },
			{ " L'", " l'" }, { " La ", " la " }, { " Als ", " als " }, { " Els ", " els " }, { " Les ", " les " },
			{ " Al ", " al " }, { " Amb ", " amb " }, { " Que ", " que " }, { " Per ", " per " }, { "l.L", "l·l" },
			{ "l·L", "l·l" } };

	/**
	 * Este método divide el string en palabras y las pasa a un arraylist
	 */
	public static ArrayList getArrayListFromString(final String cadena) {

		final ArrayList<String> lista = new ArrayList<String>();
		if (cadena.length() > 0) {
			final String txseparador = "" + Microfront.separatorwords;
			final String[] listastringcadenas = cadena.split(txseparador);
			for (final String listastringcadena : listastringcadenas) {
				if (listastringcadena.length() > 0) {
					lista.add(listastringcadena);
				}
			}
		}

		return lista;
	}

	/**
	 * Este método divide el string en palabras y las pasa a un hashtable
	 */
	public static Hashtable<String, String> getHashtableFromString(final String cadena) {
		final Hashtable<String, String> listahash = new Hashtable<String, String>();

		if (cadena.length() > 0) {
			final String txseparador = "" + Microfront.separatorwords;
			final String[] listastringcadenas = cadena.split(txseparador);
			for (final String listastringcadena : listastringcadenas) {
				if (listastringcadena.length() > 0) {
					listahash.put(listastringcadena, listastringcadena);
				}
			}
		}
		return listahash;
	}

	/**
	 * Método que pone la inicial de cada palabra en mayusculas.
	 *
	 * @param texte
	 */
	public static void initAllTab(final StringBuffer texte) {

		final int LONG = texte.length();
		boolean primer = true;
		char c;
		for (int i = 0; i < LONG; i++) {
			c = texte.charAt(i);
			if (c == ' ' || c == '.' || c == '\'' || c == '"') {
				primer = true;
			} else {
				if (primer) {
					texte.setCharAt(i, Character.toUpperCase(c));
					primer = false;
				} else {
					texte.setCharAt(i, Character.toLowerCase(c));
				}
			}
		}

	}

	protected static void replace(final String original, final StringBuffer texte, final String patro,
			final String canvi) {

		final int LONG_CANVI = canvi.length();

		if (patro.length() != LONG_CANVI) {
			throw new RuntimeException("els patrons de canvis a Format han de tenir la mateixa llargada!");
		}

		// per cada ocurrencia del canvi feim el canvi en el texte original
		for (int index = original.indexOf(patro); index != -1; index = original.indexOf(patro, index + LONG_CANVI)) {
			texte.replace(index, index + LONG_CANVI, canvi);
		}

	}

	public static String initTab(final String texte) {
		if (texte == null) {
			return null;
		}

		final StringBuffer buf = new StringBuffer(texte);
		initAllTab(buf);
		final String original = buf.toString();
		for (final String[] canvi : canvis) {
			replace(original, buf, canvi[0], canvi[1]);
		}

		return buf.toString();
	}

	/**
	 * Método convierte el stack trace de una excepcion en un string
	 *
	 * @param mensajes
	 *            vector de "StactkTraceElement"
	 * @param numelementos
	 *            número de elementos del vector de mensajes que se pasaran al
	 *            string
	 */
	public static String statcktrace2String(final StackTraceElement[] mensajes, final int numelementos) {
		final StringBuffer stlog = new StringBuffer("");
		final int mensmostrados = (mensajes.length < numelementos) ? mensajes.length : numelementos;
		for (int x = 0; x < mensmostrados; x++) {
			stlog.append(mensajes[x].getClassName());
			stlog.append(" (");
			stlog.append(mensajes[x].getMethodName());
			stlog.append(" >> linea:");
			stlog.append(mensajes[x].getLineNumber());
			stlog.append(") \n");
		}
		if (mensajes.length >= numelementos) {
			stlog.append(" (mas) ...\n ");
		}
		return stlog.toString();
	}

	/**
	 * convertir acentos en codigos html (acute / grave)
	 *
	 * @param filtro
	 * @return
	 */
	public static String convert(final String filtro) {
		return filtro.replace("á", "&aacute;").replace("Á", "&Aacute;").replace("é", "&eacute;")
				.replace("É", "&Eacute;").replace("í", "&iacute;").replace("Í", "&Iacute;").replace("ó", "&oacute;")
				.replace("Ó", "&Oacute;").replace("ú", "&uacute;").replace("Ú", "&Uacute;").replace("à", "&agrave;")
				.replace("À", "&Agrave;").replace("è", "&egrave;").replace("È", "&Egrave;").replace("ì", "&igrave;")
				.replace("Ì", "&Igrave;").replace("ò", "&ograve;").replace("Ò", "&Ograve;").replace("ù", "&ugrave;")
				.replace("Ù", "&Ugrave;");
	}

}
