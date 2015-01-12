package es.caib.gusite.microfront.util;

import java.util.ArrayList;
import java.util.Hashtable;

import es.caib.gusite.microfront.Microfront;

public class Cadenas {

	   private static String[][] canvis = new String[][] { {" I "  , " i "},
           {" O "  , " o "},
           {" De " , " de "},
           {" Del ", " del "},
           {" D'"  , " d'"},
           {" A "  , " a "},
           {" En " , " en "},
           {" El " , " el "},
           {" L'"  , " l'"},
           {" La " , " la "},
           {" Als ", " als "},
           {" Els ", " els "},
           {" Les ", " les "},
           {" Al " , " al "},
           {" Amb ", " amb "},
           {" Que ", " que "},
           {" Per ", " per "},
           {"l.L"  , "l·l"},
           {"l·L"  , "l·l"}
            };
	
	  /** 
	   * Este método divide el string en palabras y las pasa a un arraylist
	   */ 
	  public static ArrayList getArrayListFromString(String cadena) {
	  
	    ArrayList<String> lista=new ArrayList<String>();
	    if (cadena.length()>0) {
	        String txseparador = "" + Microfront.separatorwords;
	        String[] listastringcadenas = cadena.split(txseparador);
	        for (int i=0;i<listastringcadenas.length;i++)
	          if (listastringcadenas[i].length()>0) lista.add(listastringcadenas[i]);
	    }
	    
	    return lista;
	  }
	  
	  /** 
	   * Este método divide el string en palabras y las pasa a un hashtable
	   */ 
	  public static Hashtable<String, String> getHashtableFromString(String cadena) {
	    Hashtable<String, String> listahash=new Hashtable<String, String>();

	    if (cadena.length()>0) {
	        String txseparador = "" + Microfront.separatorwords;
	        String[] listastringcadenas = cadena.split(txseparador);
	        for (int i=0;i<listastringcadenas.length;i++)
	          if (listastringcadenas[i].length()>0) listahash.put(listastringcadenas[i],listastringcadenas[i]);
	    }    
	    return listahash;
	  }  	  
	  
	  /**
	   * Método que pone la inicial de cada palabra en mayusculas.
	   * @param texte
	   */
	  public static void initAllTab (StringBuffer texte) {
		      
		      final int LONG = texte.length();
		      boolean primer = true;
		      char c;
		      for (int i=0; i<LONG; i++) {
		         c = texte.charAt(i);
		         if ( c == ' ' || c == '.' || c == '\'' || c == '"' ) {
		            primer = true;
		         } else {
		            if (primer) {
		               texte.setCharAt(i,Character.toUpperCase(c));
		               primer = false;
		            } else {
		               texte.setCharAt(i,Character.toLowerCase(c));
		            }
		         }
		      }
		        
		}	  
	  
		protected static void replace(String original, StringBuffer texte,
			String patro, String canvi) {

			final int LONG_CANVI = canvi.length();
	
			if (patro.length() != LONG_CANVI) {
				throw new RuntimeException(
						"els patrons de canvis a Format han de tenir la mateixa llargada!");
			}
	
			// per cada ocurrencia del canvi feim el canvi en el texte original
			for (int index = original.indexOf(patro); index != -1; index = original
					.indexOf(patro, index + LONG_CANVI)) {
				texte.replace(index, index + LONG_CANVI, canvi);
			}

		}
		
		
		public static String initTab(String texte) {
			if (texte == null)
				return null;
	
			StringBuffer buf = new StringBuffer(texte);
			initAllTab(buf);
			String original = buf.toString();
			for (int i = 0; i < canvis.length; i++) {
				replace(original, buf, canvis[i][0], canvis[i][1]);
			}
	
			return buf.toString();
		}    
		
		/**
		 * Método convierte el stack trace de una excepcion en un string
		 * @param mensajes vector de "StactkTraceElement"
		 * @param numelementos número de elementos del vector de mensajes que se pasaran al string
		 */
		public static String statcktrace2String(StackTraceElement[] mensajes, int numelementos) {
		    StringBuffer stlog = new StringBuffer("");
		    int mensmostrados = (mensajes.length<numelementos)?mensajes.length:numelementos;
		    for (int x=0;x<mensmostrados;x++) {
		    	stlog.append(mensajes[x].getClassName());
		    	stlog.append(" (");
		    	stlog.append(mensajes[x].getMethodName());
		    	stlog.append(" >> linea:");
		    	stlog.append(mensajes[x].getLineNumber());
		    	stlog.append(") \n");
		    }
		    if (mensajes.length>=numelementos) {
		    	stlog.append(" (mas) ...\n ");
		    }
		    return stlog.toString();
		}
}
