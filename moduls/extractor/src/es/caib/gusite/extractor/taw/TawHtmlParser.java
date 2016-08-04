package es.caib.gusite.extractor.taw;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import org.fundacionctic.taw.Pautas;
import org.fundacionctic.taw.treetable.model.ComprobacionNode;
import org.fundacionctic.taw.treetable.model.ProblemaNode;
import org.fundacionctic.taw.treetable.model.PuntoVerificacionNode;
import org.fundacionctic.taw.treetable.model.URLNode;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

public class TawHtmlParser {

	private String urlanalizar = "";
	private URL url;
	private Pautas pautas;
	private Parser parser;
	private TawResultBean resultado = new TawResultBean(); //Resultado final 
	
	public TawHtmlParser() {}

    public TawResultBean parse(String url) throws Exception  {
    	urlanalizar = url;
        parseSimple();
        return resultado;
    }
	
    private void parseSimple() throws Exception  {
    	
    	pautas = new Pautas();
		pautas.setNivel(3, null, null);
		

		try {
			//url = new URL("http://vroca.indra.es/sacmicrofront/taw.do?ttr=CNTSP&idioma=ca&id=1658&idsite=109");
			
			//RECOGER URL
			url = new URL(urlanalizar);
        
	        //CONECTAR
	        if (connect()) {
			
				//ANALIZA TAW
	        	resultado.setUrlNode(new URLNode(url.toString()));
		
	        	
				for(int i = 0; i < pautas.getLength(); i++)
		            try {
		            	if (pautas.getPuntoVerificacion(i).getId().equals("3.2")) {
		            		//El punto 3.2 comprueba el doctype, que en html5 no existe.
		            		continue;
		            	}
		                pautas.getPuntoVerificacion(i).analizar(parser, resultado.getUrlNode());
		                
		            } catch(ClassCastException cEx) {
		            	//en realidad esto es un warning.
						//resultado.setMensaje("Cast Exception " + cEx.getMessage());
						
					} catch(ParserException pE) {
						//en realidad esto es un warning. 
		            	//resultado.setMensaje("Parser Exception " + pE.getMessage());
		            	
		            } catch(Error e) {
		            	resultado.setUrlNode(null);
		            	resultado.setParseado(TawResultBean.PARSED_ERROR);
		            	resultado.setMensaje("Error en  " + url.toString() + e.getMessage());
		            	break;
		            }
	
		       if (resultado.getUrlNode()!=null) {
		    	   resultado.setParseado(TawResultBean.PARSED_OK);
		    	   
		    	   //ANALIZAMOS Y, RECOGEMOS todos los automaticos y manuales, serializamos mensajes.
		           ArrayList listares1 = (ArrayList)resultado.getUrlNode().getPrioridad1().getChilds();
		           Iterator iter = (Iterator)listares1.iterator();
		           while (iter.hasNext()) {
			           	PuntoVerificacionNode pvn = (PuntoVerificacionNode)iter.next();
			           	resultado.setP1auto(resultado.getP1auto()+pvn.getAutomaticos());
			           	resultado.setP1manual(resultado.getP1manual()+pvn.getManuales());
			           	//sólo si hay algún automático, meteremos mensaje
			           	if (pvn.getAutomaticos()>0) {
				           	resultado.setMensaje(resultado.getMensaje() + "\n " + pvn.getAutomaticos() + " - " + pvn.toString());
				        	Iterator itersub = pvn.getChildVector().iterator();
				        	while (itersub.hasNext()) {
				        		ComprobacionNode cpn = (ComprobacionNode)itersub.next();
				        		resultado.setMensaje(resultado.getMensaje() + "\n     " + cpn.toString());
				        		for (int i=0;i<cpn.getNumChildren();i++) {
				        			ProblemaNode prn = (ProblemaNode)cpn.getChild(i);
				        			resultado.setMensaje(resultado.getMensaje() + "\n      " + prn.toString());
				        		}
				        	}
			           	}
		           }
		           
		           ArrayList listares2 = (ArrayList)resultado.getUrlNode().getPrioridad2().getChilds();
		           iter = (Iterator)listares2.iterator();
		           while (iter.hasNext()) {
			           	PuntoVerificacionNode pvn = (PuntoVerificacionNode)iter.next();
			           	resultado.setP2auto(resultado.getP2auto()+pvn.getAutomaticos());
			           	resultado.setP2manual(resultado.getP2manual()+pvn.getManuales());
			           	//sólo si hay algún automático, meteremos mensaje
			           	if (pvn.getAutomaticos()>0) {
				           	resultado.setMensaje(resultado.getMensaje() + "\n " + pvn.getAutomaticos() + " - " + pvn.toString());
				        	Iterator itersub = pvn.getChildVector().iterator();
				        	while (itersub.hasNext()) {
				        		ComprobacionNode cpn = (ComprobacionNode)itersub.next();
				        		resultado.setMensaje(resultado.getMensaje() + "\n     " + cpn.toString());
				        		for (int i=0;i<cpn.getNumChildren();i++) {
				        			ProblemaNode prn = (ProblemaNode)cpn.getChild(i);
				        			resultado.setMensaje(resultado.getMensaje() + "\n      " + prn.toString());
				        		}
				        	}
			           	}
		           }
		           
		           // De momento, la prioridad 3 no hacemos nada
		           /*
			           ArrayList listares3 = (ArrayList)resultado.getUrlNode().getPrioridad3().getChilds();
			           iter = (Iterator)listares3.iterator();
			           while (iter.hasNext()) {
			           	PuntoVerificacionNode pvn = (PuntoVerificacionNode)iter.next();
			           	resultado.setP3auto(resultado.getP3auto()+pvn.getAutomaticos());
			           	resultado.setP3manual(resultado.getP3manual()+pvn.getManuales());
			           	resultado.setMensaje(resultado.getMensaje() + "\n " + pvn.getAutomaticos() + " " + pvn.getManuales() + " " + pvn.toString());
			        	Iterator itersub = pvn.getChildVector().iterator();
			        	while (itersub.hasNext()) {
			        		ComprobacionNode cpn = (ComprobacionNode)itersub.next();
			        		resultado.setMensaje(resultado.getMensaje() + "\n     " + cpn.toString());
			        		for (int i=0;i<cpn.getNumChildren();i++) {
			        			ProblemaNode prn = (ProblemaNode)cpn.getChild(i);
			        			resultado.setMensaje(resultado.getMensaje() + "\n      " + prn.toString());
			        		}
			        	}
			        	}
		            */
		           
		    	   

		           resultado.setErrores(resultado.getP1auto()+resultado.getP2auto());
		           //resultado.setWarnings(resultado.getP1manual()+resultado.getP2manual());
		    	   
		       }
		    	   
		       
			} else {
				resultado.setParseado(TawResultBean.PARSED_ERROR);
			}
	        
	        
        } catch(MalformedURLException murlE) {
        	resultado.setParseado(TawResultBean.PARSED_ERROR);
        	resultado.setMensaje("Malformed URL " + murlE.getMessage());
        }

    }
 
    
    protected boolean connect()
    {
        URLConnection urlconnection = null;
        try
        {
            urlconnection = url.openConnection();
            if(urlconnection instanceof HttpURLConnection)
            {
                HttpURLConnection http = (HttpURLConnection)urlconnection;
                switch(http.getResponseCode())
                {
                case 404: 
                	resultado.setMensaje("Error: 404: Not Found");
                    return false;

                case 400: 
                	resultado.setMensaje("Error: 400: Bad Request");
                    return false;

                case 405: 
                	resultado.setMensaje("Error: 405: Method Not Allowed");
                    return false;

                case 502: 
                	resultado.setMensaje("Error: 502: Bad Gateway");
                    return false;

                case 408: 
                	resultado.setMensaje("Error: 408: Request Time-Out");
                    return false;

                case 409: 
                	resultado.setMensaje("Error: 409: Conflict");
                    return false;

                case 413: 
                	resultado.setMensaje("Error: 413: Request Entity Too Large");
                    return false;

                case 403: 
                	resultado.setMensaje("Error: 403: Forbidden");
                    return false;

                case 504: 
                	resultado.setMensaje("Error: 504: Gateway Timeout");
                    return false;

                case 410: 
                	resultado.setMensaje("Error: 410: Gone");
                    return false;

                case 500: 
                	resultado.setMensaje("Error: 500: Internal Server Error");
                    return false;

                case 411: 
                	resultado.setMensaje("Error: 411: Length Required");
                    return false;

                case 406: 
                	resultado.setMensaje("Error: 406: Not Acceptable");
                    return false;

                case 501: 
                	resultado.setMensaje("Error: 501: Not Implemented");
                    return false;

                case 402: 
                	resultado.setMensaje("Error: 402: Payment Required");
                    return false;

                case 412: 
                	resultado.setMensaje("Error: 412: Precondition Failed");
                    return false;

                case 407: 
                	resultado.setMensaje("Error: 407: Proxy Authentication Required");
                    return false;

                case 414: 
                	resultado.setMensaje("Error: 414: Request-URI Too Large");
                    return false;

                case 401: 
                	resultado.setMensaje("Error: 401: Unauthorized");
                    return false;

                case 503: 
                	resultado.setMensaje("Error: 503: Service Unavailable");
                    return false;

                case 415: 
                	resultado.setMensaje("Error: 415: Unsupported Media Type");
                    return false;

                case 505: 
                	resultado.setMensaje("Error: 505: HTTP Version Not Supported");
                    return false;

                }
            }
            String contentType = urlconnection.getContentType();
            if(contentType != null && !contentType.toLowerCase().startsWith("text/html"))
                return false;
        
            if(parser == null) parser = new Parser();
            parser.setConnection(urlconnection);
            return true;

        } catch(UnknownHostException uhE) {
        	resultado.setMensaje("Error: No html " + uhE.getMessage());
            return false;
        } catch(ConnectException cE) {
        	resultado.setMensaje("Error: Connection refused " + cE.getMessage());
        	return false;
        } catch(IOException ioE) {
        	resultado.setMensaje("Error: IO " + ioE.getMessage());
            return false;
        } catch(ParserException pE) {
        	resultado.setMensaje("Error: Parser " + pE.getMessage());
            return false;
        }

        
    }
    
    
}
